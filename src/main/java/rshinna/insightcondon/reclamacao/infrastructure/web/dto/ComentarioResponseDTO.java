package rshinna.insightcondon.reclamacao.infrastructure.web.dto;

import rshinna.insightcondon.reclamacao.domain.ComentarioReclamacao;

import java.time.Instant;

public record ComentarioResponseDTO(
        String comentarioId,
        String autorNome,
        String texto,
        Instant createdAt
) {
    public static ComentarioResponseDTO from(ComentarioReclamacao comentario, String autorNome){
        return new ComentarioResponseDTO(
                comentario.getId().toString(),
                autorNome,
                comentario.getTexto(),
                comentario.getCreatedAt()
        );
    }
}
