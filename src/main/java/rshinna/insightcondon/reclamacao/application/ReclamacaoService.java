package rshinna.insightcondon.reclamacao.application;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.categoria.application.CategoriaService;
import rshinna.insightcondon.categoria.domain.CategoriaId;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.ReclamacaoId;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;
import rshinna.insightcondon.reclamacao.domain.Urgencia;
import rshinna.insightcondon.reclamacao.infrastructure.ReclamacaoRepository;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;
import rshinna.insightcondon.shared.infrastructure.security.UsuarioAutenticado;
import rshinna.insightcondon.usuario.domain.UsuarioId;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReclamacaoService {

    private final ReclamacaoRepository reclamacaoRepository;
    private final CategoriaService categoriaService;

    public Reclamacao registrar(String titulo, String descricao, UUID categoriaId,
                                UsuarioId usuarioId, CondominioId condominioId,
                                boolean anonimo, Urgencia urgencia) {
        if (categoriaId != null) {
            categoriaService.buscarPorId(CategoriaId.de(categoriaId));
        }

        Reclamacao reclamacao = new Reclamacao(titulo, descricao, categoriaId, usuarioId.value(),
                condominioId.value(), anonimo, urgencia);

        return reclamacaoRepository.save(reclamacao);
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
