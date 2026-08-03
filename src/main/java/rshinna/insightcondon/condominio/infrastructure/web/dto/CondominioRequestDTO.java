package rshinna.insightcondon.condominio.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CondominioRequestDTO(
        @NotBlank(message = "Nome do condomínio é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @Size(max = 18, message = "CNPJ inválido")
        String cnpj,

        @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
        String endereco
) {}
