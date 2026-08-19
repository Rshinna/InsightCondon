package rshinna.insightcondon.reclamacao.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AjustarUrgenciaRequestDTO(
        @NotBlank(message = "Urgência é obrigatória")
        @Pattern(regexp = "BAIXA|MEDIA|ALTA|CRITICA", message = "Urgência inválida")
        String urgencia
) {
}
