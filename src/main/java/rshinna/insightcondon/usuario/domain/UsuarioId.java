package rshinna.insightcondon.usuario.domain;

import java.util.UUID;

public record UsuarioId(UUID value) {

    public static UsuarioId novo() {
        return new UsuarioId(UUID.randomUUID());
    }

    public static UsuarioId de(UUID value) {
        return new UsuarioId(value);
    }

    public static UsuarioId de(String value) {
        return new UsuarioId(UUID.fromString(value));
    }

    @Override
    public String toString() {

        return value.toString();
    }
}
