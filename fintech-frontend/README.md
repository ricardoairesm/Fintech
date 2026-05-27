# FinUp Frontend

Frontend React + TypeScript da aplicação Fintech, criado com Vite, React Router, Tailwind CSS e componentes no padrão [shadcn/ui](https://ui.shadcn.com/).

## Pre-requisitos

- Node.js 20.19+ e npm

## Executar

Com o backend em execução em `http://localhost:8080`, execute:

```bash
npm install
npm run dev
```

A interface inicia em `http://localhost:5173`.

Para gerar a versão de produção:

```bash
npm run build
```

## Rotas (SPA com React Router)

| Rota | Página |
| --- | --- |
| `/login` | Autenticação pela API backend |
| `/register` | Cadastro público de contas `USER` |
| `/` | Painel do usuário (resumo, finanças, conquistas, perfil) — protegida |
| `/admin` | Painel de dados com CRUD completo das 9 entidades — apenas `ADMIN` |
| `*` | Página de erro `404` personalizada |

## Telas

- Login autenticado pela API backend.
- Cadastro público de novas contas do tipo `USER`.
- Painel do usuário com resumo, transações e cadastro das próprias contas bancárias, metas e transações.
- Área de conquistas com níveis, desafios concluídos/ativos e recompensas.
- Perfil com usuário e endereço principal.
- **Painel de dados** (`/admin`), visível somente para usuários `ADMIN`, para **consultar, inserir, atualizar e remover** registros das nove entidades, consumindo os controllers REST de CRUD do backend.

## Integração com backend

Os contratos em `src/models/fintech.ts` cobrem as entidades presentes em `fintech-backend`. O cadastro consome `POST /api/auth/register`. A tela de login consome `POST /api/auth/login` e, em seguida, o dashboard consome `GET /api/users/{userId}/dashboard`. O cliente cadastra seus recursos por `POST /api/me/accounts`, `POST /api/me/goals` e `POST /api/me/transactions`, enviando o token da sessão. Para usuários `ADMIN`, a aba administrativa envia esse token ao consultar `GET /api/admin/entities` e ao enviar cadastros para `POST /api/admin/*`.

Execute a migração backend `V002__user_type_admin_access.sql` e promova ao menos uma conta para `ADMIN` antes de testar a aba administrativa.

A URL padrão é `http://localhost:8080/api`. Para usar outro backend, crie `.env` a partir de `.env.example` e altere:

```bash
VITE_API_URL=http://localhost:8080/api
```

## UI

O projeto está configurado para o shadcn/ui em `components.json`. Os componentes usados pela aplicação ficam em `src/components/ui` e podem ser expandidos com o CLI:

```bash
npx shadcn@latest add <componente>
```
