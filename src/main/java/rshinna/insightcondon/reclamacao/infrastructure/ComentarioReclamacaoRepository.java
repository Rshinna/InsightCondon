package rshinna.insightcondon.reclamacao.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import rshinna.insightcondon.reclamacao.domain.ComentarioReclamacao;

import java.util.List;
import java.util.UUID;

public interface ComentarioReclamacaoRepository extends JpaRepository<ComentarioReclamacao, UUID> {
    List<ComentarioReclamacao> findByReclamacaoIdOrderByCreatedAtAsc(UUID reclamacaoId);
}
