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

Os contratos em `src/models/fintech.ts` cobrem as entidades presentes em `fintech-backend`. Para a interface, `Goal` e `Challenge` incluem informacoes visuais de titulo e progresso que deverao ser fornecidas ou calculadas pela API. O backend atual implementa DAOs Java, mas nao disponibiliza rotas HTTP; por isso os dados ficam isolados em `src/data/demo.ts` ate a criacao de endpoints para autenticacao e consultas.

## UI

O projeto esta configurado para o shadcn/ui em `components.json`. Os componentes usados pela aplicacao ficam em `src/components/ui` e podem ser expandidos com o CLI:

```bash
npx shadcn@latest add <componente>
```
