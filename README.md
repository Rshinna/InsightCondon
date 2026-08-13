# InsightCondon

Aplicação de gestão de reclamações condominiais, com classificação automática por IA e ranking de prioridade —
construída para centralizar demandas de moradores e dar visibilidade clara aos síndicos sobre o que precisa de atenção
primeiro.

## Problema

Muitos condomínios ainda dependem de planilhas ou sistemas básicos de registro de ocorrências, dificultando a
identificação dos problemas mais críticos e a tomada de decisão eficiente.

## Solução

- **Centralização**: moradores registram reclamações via app/web
- **Classificação automática**: IA identifica tema e urgência de cada reclamação
- **Ranking de prioridade**: algoritmo pondera frequência, impacto coletivo e urgência
- **Dashboard**: visão clara dos problemas mais relevantes, com transparência para o morador acompanhar o status

## Stack

| Camada         | Tecnologia                                                                  |
|----------------|-----------------------------------------------------------------------------|
| Back-end       | Java 21 + Spring Boot 4                                                     |
| Banco de dados | PostgreSQL 16                                                               |
| Migrations     | Flyway                                                                      |
| Autenticação   | Spring Security + JWT                                                       |
| IA             | SDK oficial Anthropic (classificação de reclamações) — *em desenvolvimento* |
| Front-end      | React + Vite — *ainda não iniciado*                                         |
| Build          | Gradle                                                                      |
| Infra local    | Docker Compose                                                              |

## Arquitetura

Organizado em **DDD leve** — pacotes por contexto de domínio, cada um dividido em `domain` (entidade JPA com Rich Domain
Model), `application` (casos de uso) e `infrastructure` (repository, controller, DTOs).

```
rshinna.insightcondon
├── shared/ → exceções, segurança (JWT) e infraestrutura comuns
├── condominio/ → gestão de condomínios
├── usuario/ → autenticação, JWT, perfis (síndico, morador, admin)
├── categoria/ → categorias de reclamação (globais + específicas por condomínio)
└── reclamacao/ → núcleo do sistema: registro, comentários, transição de status
```

### Modelo de segurança

- Autenticação via **JWT stateless**, sem sessão de servidor.
- Login em **duas etapas**: verificação de e-mail (retorna os condomínios vinculados) seguida da senha, já que o mesmo
  e-mail pode ter contas em condomínios diferentes.
- Autorização por perfil (`MORADOR`, `SINDICO`, `ADMIN`) via `@PreAuthorize`, com regras adicionais de acesso (ex:
  morador só vê as próprias reclamações; comentários herdam o acesso da reclamação-mãe).
- Multi-tenancy por `condominioId`, embutido no token e validado em cada operação sensível.

## Como rodar localmente

**Pré-requisitos:** Java 21, Docker

```bash
# 1. Criar o arquivo .env na raiz (não versionado)
echo "DB_USER=postgres
DB_PASSWORD=postgres" > .env

# 2. Subir o banco de dados
docker compose up -d

# 3. Rodar a aplicação (Flyway aplica as migrations automaticamente)
./gradlew bootRun
```

A API sobe em `http://localhost:8080`.

## Roadmap

- [x] Contexto `condominio` (CRUD completo)
- [x] Contexto `usuario` (cadastro, login em 2 etapas, JWT)
- [x] Contexto `categoria` (globais + específicas, restrito a síndico/admin)
- [x] Contexto `reclamacao` (registro, listagem por perfil, anonimato, transição de status)
- [x] `ComentarioReclamacao` (histórico de andamento)
- [ ] Integração com IA para classificação automática (urgência)
- [ ] Cálculo de score de prioridade
- [ ] Dashboard (endpoints agregados)
- [ ] Front-end em React

## Sobre o projeto

Projeto pessoal de portfólio com potencial de evolução para produto real, desenvolvido com foco em boas práticas de
arquitetura, segurança e organização de código.