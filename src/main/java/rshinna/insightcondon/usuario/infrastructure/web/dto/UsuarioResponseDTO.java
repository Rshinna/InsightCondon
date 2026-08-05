package rshinna.insightcondon.usuario.infrastructure.web.dto;

import rshinna.insightcondon.usuario.domain.Perfil;
import rshinna.insightcondon.usuario.domain.Usuario;

import java.time.Instant;

public record UsuarioResponseDTO(
        String usuarioId,
        String nome,
        String email,
        String telefone,
        Perfil perfil,
        String unidade,
        String condominioId,
        Instant createdAt
) {
    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getPerfil(),
                usuario.getUnidade(),
                usuario.getCondominioId().toString(),
                usuario.getCreatedAt()
        );
    }
}