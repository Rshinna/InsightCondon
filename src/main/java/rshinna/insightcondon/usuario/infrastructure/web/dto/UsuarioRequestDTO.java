package rshinna.insightcondon.usuario.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import rshinna.insightcondon.usuario.domain.Perfil;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String senha,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @NotNull(message = "Perfil é obrigatório")
        Perfil perfil,

        @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
        String unidade,

        @NotNull(message = "Condomínio é obrigatório")
        String condominioId
) {
}