# FinUp

Aplicacao financeira composta por um frontend React/TypeScript e uma API Java para controle financeiro, metas e conquistas.

## Projetos

- `fintech-frontend`: interface React criada com Vite, Tailwind CSS e componentes shadcn/ui.
- `fintech-backend`: API REST Java 17 com Javalin e persistencia Oracle via JDBC.

## Executar Backend

```bash
cd fintech-backend
cp .env.example .env
# Preencha as credenciais Oracle no arquivo .env.
mvn exec:java
```

Consulte [fintech-backend/README.md](fintech-backend/README.md) para aplicar a migracao de banco e conhecer os endpoints.

## Executar Frontend

```bash
cd fintech-frontend
npm install
npm run dev
```

A interface inicia em `http://localhost:5173` e a API, por padrao, em `http://localhost:8080`.
