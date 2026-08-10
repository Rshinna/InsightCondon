package rshinna.insightcondon.categoria.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
    @NotBlank(message = "Nome da categoria é obrigatório")
        @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
        String nome,
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres") String descricao) {}
