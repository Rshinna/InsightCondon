package rshinna.insightcondon.reclamacao.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.categoria.application.CategoriaService;
import rshinna.insightcondon.categoria.domain.CategoriaId;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.reclamacao.domain.ClassificacaoIA;
import rshinna.insightcondon.reclamacao.domain.ClassificadorDeReclamacao;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.ReclamacaoId;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;
import rshinna.insightcondon.reclamacao.domain.Urgencia;
import rshinna.insightcondon.reclamacao.infrastructure.ReclamacaoRepository;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;
import rshinna.insightcondon.shared.infrastructure.security.UsuarioAutenticado;
import rshinna.insightcondon.usuario.domain.UsuarioId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReclamacaoService {

    private final ReclamacaoRepository reclamacaoRepository;
    private final CategoriaService categoriaService;
    private final ClassificadorDeReclamacao classificadorDeReclamacao;
    private final ScoreCalculadorService scoreCalculadorService;

    public Reclamacao registrar(String titulo, String descricao, UUID categoriaId,
                                UsuarioId usuarioId, CondominioId condominioId,
                                boolean anonimo, Urgencia urgencia) {
        if (categoriaId != null) {
            categoriaService.buscarPorId(CategoriaId.de(categoriaId));
        }

        Reclamacao reclamacao = new Reclamacao(titulo, descricao, categoriaId, usuarioId.value(),
                condominioId.value(), anonimo, urgencia);

        Reclamacao salva = reclamacaoRepository.save(reclamacao);

        classificarComIA(salva);
        recalcularScore(salva);

        return salva;
    }

    private void recalcularScore(Reclamacao reclamacao) {
        BigDecimal novoScore = scoreCalculadorService.calcular(reclamacao);
        reclamacao.atualizarScorePrioridade(novoScore);
    }

    public Reclamacao ajustarUrgencia(ReclamacaoId reclamacaoId, Urgencia novaUrgencia) {
        Reclamacao reclamacao = buscarPorId(reclamacaoId);
        reclamacao.ajustarUrgencia(novaUrgencia);
        recalcularScore(reclamacao);
        return reclamacao;
    }

    private void classificarComIA(Reclamacao reclamacao) {
        try {
            ClassificacaoIA classificacao = classificadorDeReclamacao.classificar(
                    reclamacao.getTitulo(), reclamacao.getDescricao());

            if (classificacao.urgenciaSugerida() != null) {
                reclamacao.aplicarSugestaoIa(null, classificacao.urgenciaSugerida());
            }
        } catch (Exception e) {
            log.warn("Não foi possível classificar a reclamação {} via IA: {}",
                    reclamacao.getId(), e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Reclamacao buscarPorId(ReclamacaoId reclamacaoId) {
        return reclamacaoRepository.findById(reclamacaoId.value())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reclamação não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Reclamacao> listarParaSindicoOuAdmin(CondominioId condominioId) {
        return reclamacaoRepository.findByCondominioIdOrderByScorePrioridadeDesc(condominioId.value());
    }

    @Transactional(readOnly = true)
    public List<Reclamacao> listarParaMorador(UsuarioId usuarioId) {
        return reclamacaoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId.value());
    }

    public Reclamacao alterarStatus(ReclamacaoId reclamacaoId, StatusReclamacao novoStatus) {
        Reclamacao reclamacao = buscarPorId(reclamacaoId);
        reclamacao.alterarStatus(novoStatus);
        return reclamacao;
    }

    public void validarAcesso(Reclamacao reclamacao, UsuarioAutenticado usuarioAutenticado) {
        boolean mesmoCondominio = reclamacao.getCondominioId().equals(usuarioAutenticado.condominioId());
        boolean ehMorador = usuarioAutenticado.perfil().equals("MORADOR");
        boolean ehAutor = reclamacao.getUsuarioId().equals(usuarioAutenticado.usuarioId());

        if (!mesmoCondominio || (ehMorador && !ehAutor)) {
            throw new AccessDeniedException("Você não tem permissão para acessar esta reclamação");
        }
    }
}
