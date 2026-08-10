package rshinna.insightcondon.shared.infrastructure.security;

import java.util.UUID;

public record UsuarioAutenticado(UUID usuarioId, UUID condominioId, String perfil) {}
