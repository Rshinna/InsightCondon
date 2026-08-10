CREATE TABLE categoria
(
    id            UUID PRIMARY KEY,
    nome          VARCHAR(80) NOT NULL,
    descricao     VARCHAR(255),
    condominio_id UUID REFERENCES condominio (id)
);

CREATE INDEX idx_categoria_condominio ON categoria (condominio_id);

INSERT INTO categoria (id, nome, descricao, condominio_id)
VALUES (gen_random_uuid(), 'Infiltração', 'Vazamentos, infiltrações e problemas de umidade', NULL),
       (gen_random_uuid(), 'Barulho', 'Ruídos excessivos, perturbação do sossego', NULL),
       (gen_random_uuid(), 'Segurança', 'Portaria, câmeras, controle de acesso, ocorrências de segurança', NULL),
       (gen_random_uuid(), 'Limpeza', 'Áreas comuns, coleta de lixo, manutenção de limpeza', NULL),
       (gen_random_uuid(), 'Manutenção', 'Elevadores, elétrica, hidráulica, estrutura predial', NULL),
       (gen_random_uuid(), 'Financeiro', 'Cobranças, boletos, prestação de contas', NULL),
       (gen_random_uuid(), 'Estacionamento', 'Vagas, garagem, veículos mal estacionados', NULL),
       (gen_random_uuid(), 'Áreas de Lazer', 'Piscina, salão de festas, academia, playground', NULL),
       (gen_random_uuid(), 'Convivência', 'Conflitos entre moradores, uso indevido de espaços', NULL),
       (gen_random_uuid(), 'Outros', 'Assuntos que não se enquadram nas demais categorias', NULL);