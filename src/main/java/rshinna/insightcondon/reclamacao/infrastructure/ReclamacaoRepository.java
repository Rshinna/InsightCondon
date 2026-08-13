package rshinna.insightcondon.reclamacao.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReclamacaoRepository extends JpaRepository<Reclamacao, UUID> {

    List<Reclamacao> findByCondominioIdOrderByScorePrioridadeDesc(UUID condominioId);

    List<Reclamacao> findByCondominioIdAndStatusOrderByScorePrioridadeDesc(UUID condominioId, StatusReclamacao status);

    List<Reclamacao> findByUsuarioIdOrderByCreatedAtDesc(UUID usuarioId);

    long countByCategoriaIdAndCreatedAtAfter(UUID categoriaId, Instant desde);
}
