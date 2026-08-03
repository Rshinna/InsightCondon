CREATE TABLE condominio (
                            id          UUID PRIMARY KEY,
                            nome        VARCHAR(150) NOT NULL,
                            cnpj        VARCHAR(18),
                            endereco    VARCHAR(255),
                            created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);