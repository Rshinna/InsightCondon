package rshinna.insightcondon.reclamacao.domain;

import java.util.UUID;

public record ComentarioId(UUID value) {

    public static ComentarioId novo() {
        return new ComentarioId(UUID.randomUUID());
    }

    public static ComentarioId de(UUID value) {
        return new ComentarioId(value);
    }

    public static ComentarioId de(String value) {
        return new ComentarioId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}