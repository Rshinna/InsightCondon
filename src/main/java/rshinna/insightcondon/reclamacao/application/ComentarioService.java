package rshinna.insightcondon.reclamacao.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.reclamacao.domain.ComentarioReclamacao;
import rshinna.insightcondon.reclamacao.domain.ReclamacaoId;
import rshinna.insightcondon.reclamacao.infrastructure.ComentarioReclamacaoRepository;
import rshinna.insightcondon.usuario.domain.UsuarioId;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComentarioService {

    private final ComentarioReclamacaoRepository comentarioReclamacaoRepository;

    public ComentarioReclamacao adicionar(ReclamacaoId reclamacaoId, UsuarioId usuarioId, String texto) {
        ComentarioReclamacao comentario = new ComentarioReclamacao(
                reclamacaoId.value(), usuarioId.value(), texto);
        return comentarioReclamacaoRepository.save(comentario);
    }

    @Transactional(readOnly = true)
    public List<ComentarioReclamacao> listarPorReclamacao(ReclamacaoId reclamacaoId) {
        return comentarioReclamacaoRepository.findByReclamacaoIdOrderByCreatedAtAsc(reclamacaoId.value());
    }
}
