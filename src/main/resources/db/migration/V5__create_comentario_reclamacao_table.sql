CREATE TABLE comentario_reclamacao
(
    id            UUID PRIMARY KEY,
    reclamacao_id UUID      NOT NULL REFERENCES reclamacao (id) ON DELETE CASCADE,
    usuario_id    UUID      NOT NULL REFERENCES usuario (id),
    texto         TEXT      NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_comentario_reclamacao ON comentario_reclamacao (reclamacao_id, created_at);