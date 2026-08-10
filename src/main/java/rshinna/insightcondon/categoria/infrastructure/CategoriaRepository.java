package rshinna.insightcondon.categoria.infrastructure;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rshinna.insightcondon.categoria.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

  @Query("SELECT c FROM Categoria c WHERE c.condominioId IS NULL OR c.condominioId = :condominioId")
  List<Categoria> findGlobaisECondominio(@Param("condominioId") UUID condominioId);

  List<Categoria> findByCondominioIdIsNull();
}
