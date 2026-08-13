CREATE TABLE reclamacao
(
    id                   UUID PRIMARY KEY,
    titulo               VARCHAR(150) NOT NULL,
    descricao            TEXT         NOT NULL,
    categoria_id         UUID REFERENCES categoria (id),
    usuario_id           UUID         NOT NULL REFERENCES usuario (id),
    condominio_id        UUID         NOT NULL REFERENCES condominio (id),
    anonimo              BOOLEAN      NOT NULL DEFAULT FALSE,
    status               VARCHAR(20)  NOT NULL DEFAULT 'ABERTA'
        CHECK (status IN ('ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'ARQUIVADA')),
    urgencia             VARCHAR(10)  NOT NULL DEFAULT 'MEDIA'
        CHECK (urgencia IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA')),
    urgencia_sugerida_ia VARCHAR(10)
        CHECK (urgencia_sugerida_ia IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA')),
    score_prioridade     NUMERIC(10, 2)        DEFAULT 0,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
    resolved_at          TIMESTAMP
);

CREATE INDEX idx_reclamacao_condominio_status ON reclamacao (condominio_id, status);
CREATE INDEX idx_reclamacao_categoria_created ON reclamacao (categoria_id, created_at);
CREATE INDEX idx_reclamacao_score ON reclamacao (condominio_id, score_prioridade DESC);
CREATE INDEX idx_reclamacao_usuario ON reclamacao (usuario_id);