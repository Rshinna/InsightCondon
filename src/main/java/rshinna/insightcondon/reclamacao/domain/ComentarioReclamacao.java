package rshinna.insightcondon.reclamacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comentario_reclamacao")
@Getter
@NoArgsConstructor
public class ComentarioReclamacao {

    @Id
    @Column(name = "id")
    private UUID comentarioId;

    @Column(name = "reclamacao_id", nullable = false)
    private UUID reclamacaoId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ComentarioReclamacao(UUID reclamacaoId, UUID usuarioId, String texto) {
        if (reclamacaoId == null) {
            throw new IllegalArgumentException("Reclamação é obrigatória");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Texto do comentário é obrigatório");
        }

        this.comentarioId = UUID.randomUUID();
        this.reclamacaoId = reclamacaoId;
        this.usuarioId = usuarioId;
        this.texto = texto;
        this.createdAt = Instant.now();
    }

    public ComentarioId getId() {
        return ComentarioId.de(this.comentarioId);
    }
}