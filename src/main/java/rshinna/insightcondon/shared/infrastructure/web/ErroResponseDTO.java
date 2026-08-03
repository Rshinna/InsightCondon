package rshinna.insightcondon.shared.infrastructure.web;

import java.time.Instant;
import java.util.List;

public record ErroResponseDTO(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes
) {
    public static ErroResponseDTO of(int status, String erro, String mensagem) {
        return new ErroResponseDTO(Instant.now(), status, erro, mensagem, List.of());
    }

    public static ErroResponseDTO of(int status, String erro, String mensagem, List<String> detalhes) {
        return new ErroResponseDTO(Instant.now(), status, erro, mensagem, detalhes);
    }
}
