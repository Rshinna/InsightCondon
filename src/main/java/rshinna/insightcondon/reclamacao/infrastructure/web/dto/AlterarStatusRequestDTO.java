package rshinna.insightcondon.reclamacao.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AlterarStatusRequestDTO(
        @NotBlank(message = "Status é obrigatório")
        @Pattern(regexp = "ABERTA|EM_ANDAMENTO|RESOLVIDA|ARQUIVADA", message = "Status inválido")
        String status
) {
}
