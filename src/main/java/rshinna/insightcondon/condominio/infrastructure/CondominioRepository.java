package rshinna.insightcondon.condominio.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import rshinna.insightcondon.condominio.domain.Condominio;

import java.util.UUID;

public interface CondominioRepository extends JpaRepository<Condominio, UUID> {
    boolean existsByCnpj(String cnpj);
}
