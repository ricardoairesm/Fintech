# Fintech Backend API

API REST Java 17 para alimentar o frontend FinUp. Construída com **Spring Boot 3** (Spring Web + Spring Data JPA), Oracle JDBC e carregamento de `.env` via `spring-dotenv`. As camadas seguem o padrão **Entity → Repository (JPA) → Service → Controller (`@RestController`)** com CRUD completo (GET/POST/PUT/DELETE) por entidade.

## Pre-requisitos

- Java 17 (JDK)
- Maven 3.9+
- Acesso a um schema Oracle

## Configuracao

As credenciais do banco não ficam no código-fonte. Crie um arquivo `.env` local a partir de `.env.example`:

```bash
cp .env.example .env
```

Preencha `DB_USER` e `DB_PASSWORD` no arquivo `.env`. Ele está ignorado pelo Git e é carregado automaticamente pela biblioteca `dotenv-java` ao iniciar a aplicação. Variáveis de ambiente definidas pelo sistema possuem precedência sobre os valores do arquivo, permitindo configuração segura em deploy.

Antes do primeiro uso da aplicação, execute no schema Oracle as migrações:

```text
src/main/resources/db/migration/V001__frontend_dashboard_fields.sql
src/main/resources/db/migration/V002__user_type_admin_access.sql
src/main/resources/db/migration/V003__transaction_goal_link.sql
```

`V001` adiciona os campos de metas e desafios consumidos pela interface React. `V002` adiciona `user_type`, inicialmente preenchido como `USER`. `V003` adiciona a coluna `goal_id` em `transactions`, usada para vincular transações de investimento a uma meta e atualizar seu `saved_amount`. Após executar `V002`, promova uma conta existente para administrar o aplicativo:

```sql
UPDATE users SET user_type = 'ADMIN' WHERE email = 'seu-email-admin@dominio.com';
COMMIT;
```

## Executar

```bash
mvn spring-boot:run
```

A verificação pública de disponibilidade não depende do banco:

```bash
curl http://localhost:8080/api/health
```

## Endpoints

| Metodo | Endpoint | Uso no frontend |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Login com `{"email":"...","password":"..."}` |
| `POST` | `/api/auth/register` | Cria uma conta do tipo `USER` |
| `POST` | `/api/auth/logout` | Encerra a sessão em memória |
| `GET` | `/api/users/{userId}/dashboard` | Carga completa da home screen |
| `GET` | `/api/users/{userId}` | Perfil do usuário sem senha |
| `GET` | `/api/users/{userId}/address` | Endereço principal |
| `GET` | `/api/users/{userId}/accounts` | Contas bancárias |
| `GET` | `/api/users/{userId}/transactions` | Transações |
| `GET` | `/api/users/{userId}/goals` | Metas financeiras |
| `GET` | `/api/users/{userId}/completed-challenges` | Desafios concluídos |
| `POST` | `/api/me/accounts` | Cadastra conta bancária do usuário autenticado |
| `POST` | `/api/me/goals` | Cadastra meta do usuário autenticado |
| `POST` | `/api/me/transactions` | Cadastra transação do usuário autenticado |
| `GET` | `/api/tiers` | Níveis de pontuação |
| `GET` | `/api/challenges` | Desafios disponíveis |
| `GET` | `/api/rewards` | Recompensas |
| `GET` | `/api/health` | Status do servidor |
| `GET` | `/api/admin/entities` | Lista todas as nove entidades; exige `ADMIN` |
| `POST` | `/api/admin/users` | Cadastra usuário; exige `ADMIN` |
| `POST` | `/api/admin/addresses` | Cadastra endereço |
| `POST` | `/api/admin/accounts` | Cadastra conta bancária |
| `POST` | `/api/admin/transactions` | Cadastra transação |
| `POST` | `/api/admin/goals` | Cadastra meta |
| `POST` | `/api/admin/tiers` | Cadastra nível |
| `POST` | `/api/admin/rewards` | Cadastra recompensa |
| `POST` | `/api/admin/challenges` | Cadastra desafio |
| `POST` | `/api/admin/completed-challenges` | Cadastra desafio concluído |

`/api/users/{userId}/dashboard` agrega os nove recursos exibidos pelo frontend: usuário, endereço, contas, transações, metas, níveis, recompensas, desafios e desafios concluídos, além de um `summary` com saldo (receitas − despesas − investimentos) e os totais por tipo.

### CRUD completo por entidade (`@RestController`)

Cada entidade expõe um controller REST com os quatro verbos e códigos de status adequados (`GET` 200, `POST` 201, `PUT` 200, `DELETE` 204; 400/404 para erros):

| Base | Entidade |
| --- | --- |
| `/api/users` | Usuários |
| `/api/addresses` | Endereços |
| `/api/bank-accounts` | Contas bancárias |
| `/api/transactions` | Transações |
| `/api/goals` | Metas |
| `/api/tiers` | Níveis |
| `/api/rewards` | Recompensas |
| `/api/challenges` | Desafios |
| `/api/completed-challenges` | Desafios concluídos |

Padrão por base: `GET /api/{base}` (lista, aceita `?userId=` onde aplicável), `GET /api/{base}/{id}`, `POST /api/{base}`, `PUT /api/{base}/{id}`, `DELETE /api/{base}/{id}`. O painel de administração do frontend consome esses endpoints para consultar, inserir, atualizar e remover registros.

## Autenticação E Administração

O login emite um token de sessão mantido em memória enquanto a API estiver executando. O frontend envia esse token como `Authorization: Bearer <token>` nas rotas `/api/admin/*`, que recusam usuários que não tenham `user_type = 'ADMIN'`.

As rotas `/api/me/accounts`, `/api/me/goals` e `/api/me/transactions` também exigem o token, mas aceitam qualquer usuário autenticado e vinculam o novo registro ao próprio usuário da sessão.

O modelo ainda armazena senha como texto. Antes de produção, a persistência deve migrar para hashes de senha e a sessão em memória deve ser substituída por uma estratégia durável e segura (ex.: JWT + Spring Security).
