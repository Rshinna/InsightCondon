package rshinna.insightcondon.reclamacao.domain;

import java.util.UUID;

public record ReclamacaoId(UUID value) {
    public static ReclamacaoId novo() {
        return new ReclamacaoId(UUID.randomUUID());
    }

    public static ReclamacaoId de(UUID value) {
        return new ReclamacaoId(value);
    }

    public static ReclamacaoId de(String value) {
        return new ReclamacaoId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
