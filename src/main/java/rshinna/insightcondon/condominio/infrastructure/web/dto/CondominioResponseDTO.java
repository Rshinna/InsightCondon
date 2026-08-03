package rshinna.insightcondon.condominio.infrastructure.web.dto;

import rshinna.insightcondon.condominio.domain.Condominio;

import java.time.Instant;

public record CondominioResponseDTO(
        String condominioId,
        String nome,
        String cnpj,
        String endereco,
        Instant createdAt
) {
    public static CondominioResponseDTO from(Condominio condominio) {
        return new CondominioResponseDTO(
                condominio.getId().toString(),
                condominio.getNome(),
                condominio.getCnpj(),
                condominio.getEndereco(),
                condominio.getCreatedAt()
        );
    }
}
