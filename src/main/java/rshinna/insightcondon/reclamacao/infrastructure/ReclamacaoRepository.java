package rshinna.insightcondon.reclamacao.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReclamacaoRepository extends JpaRepository<Reclamacao, UUID> {

    List<Reclamacao> findByCondominioIdOrderByScorePrioridadeDesc(UUID condominioId);

    List<Reclamacao> findByCondominioIdAndStatusOrderByScorePrioridadeDesc(UUID condominioId, StatusReclamacao status);

    List<Reclamacao> findByUsuarioIdOrderByCreatedAtDesc(UUID usuarioId);

    @Query("""
            SELECT COUNT(DISTINCT r.usuarioId) FROM Reclamacao r
            WHERE r.categoriaId = :categoriaId
            AND r.status IN ('ABERTA', 'EM_ANDAMENTO')
            AND r.createdAt >= :desde
            """)
    long countUsuariosDistintosAtivosPorCategoria(@Param("categoriaId") UUID categoriaId,
                                                  @Param("desde") Instant desde);

    @Query("""
            SELECT COUNT(r) FROM Reclamacao r
            WHERE r.categoriaId = :categoriaId
            AND r.status IN ('ABERTA', 'EM_ANDAMENTO')
            AND r.createdAt >= :desde
            """)
    long countAtivasPorCategoria(@Param("categoriaId") UUID categoriaId,
                                 @Param("desde") Instant desde);
}
