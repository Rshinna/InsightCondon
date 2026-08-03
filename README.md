# InsightCondon

Aplicação de gestão de reclamações condominiais, com classificação automática por IA e ranking de prioridade — construída para centralizar demandas de moradores e dar visibilidade clara aos síndicos sobre o que precisa de atenção primeiro.

## Problema

Muitos condomínios ainda dependem de planilhas ou sistemas básicos de registro de ocorrências, dificultando a identificação dos problemas mais críticos e a tomada de decisão eficiente.

## Solução

- **Centralização**: moradores registram reclamações via app/web
- **Classificação automática**: IA identifica tema e urgência de cada reclamação
- **Ranking de prioridade**: algoritmo pondera frequência, impacto coletivo e urgência
- **Dashboard**: visão clara dos problemas mais relevantes, com transparência para o morador acompanhar o status

## Stack

| Camada | Tecnologia |
|---|---|
| Back-end | Java 21 + Spring Boot 4 |
| Banco de dados | PostgreSQL 16 |
| Migrations | Flyway |
| Autenticação | Spring Security + JWT |
| IA | SDK oficial Anthropic (classificação de reclamações) |
| Front-end | React + Vite *(em desenvolvimento)* |
| Build | Gradle |
| Infra local | Docker Compose |

## Arquitetura

Organizado em **DDD leve** — pacotes por contexto de domínio (`condominio`, `usuario`, `categoria`, `reclamacao`), cada um dividido em `domain` (entidade JPA com Rich Domain Model), `application` (casos de uso) e `infrastructure` (repository, controller, DTOs).
```
rshinna.insightcondon
├── shared/ → exceções e infraestrutura comuns
├── condominio/ → gestão de condomínios
├── usuario/ → autenticação e perfis (síndico, morador, admin)
├── categoria/ → categorias de reclamação
└── reclamacao/ → núcleo do sistema: registro, classificação e priorização
```
## Como rodar localmente

**Pré-requisitos:** Java 21, Docker

```bash
# 1. Subir o banco de dados
docker compose up -d

# 2. Rodar a aplicação (Flyway aplica as migrations automaticamente)
./gradlew bootRun
```

A API sobe em `http://localhost:8080`.

## Roadmap

- [x] Contexto `condominio` (CRUD completo)
- [ ] Contexto `usuario` (autenticação, JWT, perfis)
- [ ] Contexto `categoria`
- [ ] Contexto `reclamacao` (registro, comentários, status)
- [ ] Integração com IA para classificação automática (tema + urgência)
- [ ] Cálculo de score de prioridade
- [ ] Dashboard (endpoints agregados)
- [ ] Front-end em React

## Sobre o projeto

Projeto pessoal de portfólio com potencial de evolução para produto real, desenvolvido com foco em boas práticas de arquitetura, segurança e organização de código.

