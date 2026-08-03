package rshinna.insightcondon.condominio.domain;

import java.util.UUID;

public record CondominioId(UUID value) {

    public static CondominioId novo(){
        return new CondominioId(UUID.randomUUID());
    }

    public static CondominioId de(UUID value){
        return new CondominioId(value);
    }

    public static CondominioId de(String value) {
        return new CondominioId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
