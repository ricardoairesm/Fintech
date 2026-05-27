# FinUp

Aplicacao financeira composta por um frontend React/TypeScript e uma API Java para controle financeiro, metas e conquistas.

## Projetos

- `fintech-frontend`: interface React (SPA) criada com Vite, React Router, Tailwind CSS e componentes shadcn/ui.
- `fintech-backend`: API REST Java 17 com Spring Boot 3 (Spring Web + Spring Data JPA) e persistencia Oracle.

## Pre-requisitos

- Java 17 (JDK) e Maven 3.9+
- Node.js 20+ e npm
- Acesso a um schema Oracle (credenciais para o `.env` do backend)

## 1. Executar o Backend

Rode o backend primeiro, pois o frontend consome a API em `http://localhost:8080`.

```bash
cd fintech-backend
cp .env.example .env
# Preencha DB_USER e DB_PASSWORD (e DB_URL, se necessario) no arquivo .env.
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. Verifique com `curl http://localhost:8080/api/health`.

Antes do primeiro uso, aplique as migracoes `V001`, `V002` e `V003` no schema Oracle. Consulte [fintech-backend/README.md](fintech-backend/README.md) para o passo a passo das migracoes e a lista de endpoints.

## 2. Executar o Frontend

Com o backend em execucao, em outro terminal:

```bash
cd fintech-frontend
npm install
npm run dev
```

A interface inicia em `http://localhost:5173`. Para apontar para outro backend, crie `.env` a partir de `.env.example` e ajuste `VITE_API_URL`.

Rotas da SPA: `/login`, `/register`, `/` (painel do usuário), `/admin` (painel de dados com CRUD completo, só para `ADMIN`) e página de erro `404` para qualquer rota inexistente.
