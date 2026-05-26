# Fintech Backend API

API REST Java 17 para alimentar o frontend FinUp. O projeto utiliza Javalin, Jackson e os DAOs JDBC/Oracle existentes.

## Configuracao

As credenciais do banco nao ficam no codigo-fonte. Crie um arquivo `.env` local a partir de `.env.example`:

```bash
cp .env.example .env
```

Preencha `DB_USER` e `DB_PASSWORD` no arquivo `.env`. Ele esta ignorado pelo Git e e carregado automaticamente pela biblioteca `dotenv-java` ao iniciar a aplicacao. Variaveis de ambiente definidas pelo sistema possuem precedencia sobre os valores do arquivo, permitindo configuracao segura em deploy.

Antes do primeiro uso do dashboard, execute no schema Oracle a migracao:

```text
src/main/resources/db/migration/V001__frontend_dashboard_fields.sql
```

Ela adiciona `title` e `saved_amount` a `goal`, e `title` e `progress` a `challenge`, campos consumidos pela interface React.

## Executar

```bash
mvn test
mvn exec:java
```

A verificacao publica de disponibilidade nao depende do banco:

```bash
curl http://localhost:8080/api/health
```

## Endpoints

| Metodo | Endpoint | Uso no frontend |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Login com `{"email":"...","password":"..."}` |
| `GET` | `/api/users/{userId}/dashboard` | Carga completa da home screen |
| `GET` | `/api/users/{userId}` | Perfil do usuario sem senha |
| `GET` | `/api/users/{userId}/address` | Endereco principal |
| `GET` | `/api/users/{userId}/accounts` | Contas bancarias |
| `GET` | `/api/users/{userId}/transactions` | Transacoes |
| `GET` | `/api/users/{userId}/goals` | Metas financeiras |
| `GET` | `/api/users/{userId}/completed-challenges` | Desafios concluidos |
| `GET` | `/api/tiers` | Niveis de pontuacao |
| `GET` | `/api/challenges` | Desafios disponiveis |
| `GET` | `/api/rewards` | Recompensas |
| `GET` | `/api/health` | Status do servidor |

`/api/users/{userId}/dashboard` agrega os nove recursos exibidos pelo frontend: usuario, endereco, contas, transacoes, metas, tiers, recompensas, desafios e desafios concluidos.

## Observacao De Autenticacao

O model original armazena senha como texto e a rota de login apenas preserva esse comportamento sem expor a senha na resposta. Antes de producao, a persistencia deve migrar para hashes de senha e a API deve emitir uma sessao ou token autenticado.
