# FinUp Frontend

Frontend React + TypeScript da aplicacao Fintech, criado com Vite, Tailwind CSS e componentes no padrao [shadcn/ui](https://ui.shadcn.com/).

## Executar

```bash
npm install
npm run dev
```

Para gerar a versao de producao:

```bash
npm run build
```

## Telas

- Login com fluxo de entrada para a demonstracao.
- Dashboard com resumo, transacoes, contas bancarias e metas.
- Area de conquistas com tiers, desafios concluidos/ativos e recompensas.
- Perfil com usuario e endereco principal.

## Integracao com backend

Os contratos em `src/models/fintech.ts` cobrem as entidades presentes em `fintech-backend`. Para a interface, `Goal` e `Challenge` incluem informacoes visuais de titulo e progresso fornecidas pela API. A API REST ja expoe `POST /api/auth/login` e `GET /api/users/{userId}/dashboard`; os dados em `src/data/demo.ts` serao substituidos por essas chamadas na proxima integracao.

## UI

O projeto esta configurado para o shadcn/ui em `components.json`. Os componentes usados pela aplicacao ficam em `src/components/ui` e podem ser expandidos com o CLI:

```bash
npx shadcn@latest add <componente>
```
