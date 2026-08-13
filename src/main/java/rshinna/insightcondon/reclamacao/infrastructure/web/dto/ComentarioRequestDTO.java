package rshinna.insightcondon.reclamacao.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequestDTO(
        @NotBlank(message = "Texto do comentário é obrigatório")
        String texto
) {
}
