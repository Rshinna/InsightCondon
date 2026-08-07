package rshinna.insightcondon.usuario.infrastructure.web.dto;

import java.util.List;

public record VerificarEmailResponseDTO(
        boolean multiplasContas,
        List<CondominioResumoDTO> condominios
) {

    public record CondominioResumoDTO(
            String condominioId,
            String nomeCondominio
    ) {
    }
}
