package rshinna.insightcondon.usuario.infrastructure.web.dto;

public record LoginResponseDTO(
        String token,
        String usuarioId,
        String nome,
        String perfil,
        String condominioId
) {
}
