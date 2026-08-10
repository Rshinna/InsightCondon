package rshinna.insightcondon.categoria.domain;

import java.util.UUID;

public record CategoriaId(UUID value) {

    public static CategoriaId novo() {
        return new CategoriaId(UUID.randomUUID());
    }

    public static CategoriaId de(UUID value) {
        return new CategoriaId(value);
    }

    public static CategoriaId de(String value) {
        return new CategoriaId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
