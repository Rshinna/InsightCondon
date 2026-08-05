package rshinna.insightcondon.usuario.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import rshinna.insightcondon.usuario.domain.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    List<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndCondominioId(String email, UUID condominioId);

    boolean existsByEmailAndCondominioId(String email, UUID condominioId);
}
