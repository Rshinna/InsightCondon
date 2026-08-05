CREATE TABLE usuario
(
    id            UUID PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    senha_hash    VARCHAR(255) NOT NULL,
    telefone      VARCHAR(20),
    perfil        VARCHAR(20)  NOT NULL CHECK (perfil IN ('SINDICO', 'MORADOR', 'ADMIN')),
    unidade       VARCHAR(20),
    condominio_id UUID         NOT NULL REFERENCES condominio (id),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_usuario_email_condominio UNIQUE (email, condominio_id)
);

CREATE INDEX idx_usuario_email ON usuario (email);