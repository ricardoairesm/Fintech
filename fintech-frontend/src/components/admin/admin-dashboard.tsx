import { type FormEvent, useEffect, useMemo, useState } from 'react'
import { Database, LoaderCircle, Plus, RefreshCw, ShieldCheck } from 'lucide-react'

import { createAdminEntity, loadAdminEntities } from '@/api/fintech'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import type { AdminEntities } from '@/models/fintech'

type AdminEntityKey = keyof AdminEntities
type FieldType = 'text' | 'password' | 'number' | 'date' | 'boolean'
type Row = Record<string, string | number | boolean | null>

interface FieldConfig {
  name: string
  label: string
  type: FieldType
  optional?: boolean
  checked?: boolean
}

interface EntityConfig {
  key: AdminEntityKey
  endpoint: string
  label: string
  description: string
  columns: Array<{ name: string; label: string }>
  fields: FieldConfig[]
}

const entities: EntityConfig[] = [
  {
    key: 'users',
    endpoint: 'users',
    label: 'Usuarios',
    description: 'Clientes cadastrados no aplicativo',
    columns: columns(['id', 'username', 'email', 'userType', 'tierId', 'points']),
    fields: [
      field('username', 'Nome'),
      field('password', 'Senha', 'password'),
      field('email', 'E-mail'),
      field('celphone', 'Telefone'),
      field('userType', 'Tipo (USER ou ADMIN)'),
      field('tierId', 'ID do nível', 'number'),
      field('points', 'Pontos', 'number'),
      field('mainAddressId', 'ID do endereço principal', 'number', true),
      field('monthlyIncome', 'Renda mensal', 'number'),
      field('monthlySpending', 'Gasto mensal', 'number'),
    ],
  },
  {
    key: 'addresses',
    endpoint: 'addresses',
    label: 'Endereços',
    description: 'Endereços vinculados aos usuários',
    columns: columns(['id', 'userId', 'addressString', 'zipCode']),
    fields: [field('userId', 'ID do usuário', 'number'), field('addressString', 'Endereço'), field('zipCode', 'CEP')],
  },
  {
    key: 'accounts',
    endpoint: 'accounts',
    label: 'Contas',
    description: 'Contas bancárias integradas',
    columns: columns(['id', 'userId', 'bank', 'type', 'accountNumber']),
    fields: [
      field('userId', 'ID do usuário', 'number'),
      field('bank', 'Banco'),
      field('type', 'Tipo'),
      field('description', 'Descrição'),
      field('agency', 'Agência'),
      field('accountNumber', 'Numero da conta'),
    ],
  },
  {
    key: 'transactions',
    endpoint: 'transactions',
    label: 'Transações',
    description: 'Entradas, saídas e investimentos',
    columns: columns(['id', 'userId', 'description', 'type', 'amount', 'transactionDate']),
    fields: [
      field('userId', 'ID do usuário', 'number'),
      field('amount', 'Valor', 'number'),
      field('type', 'Tipo'),
      field('description', 'Descrição'),
      field('transactionDate', 'Data', 'date'),
      field('bankAccountId', 'ID da conta', 'number'),
      field('yield', 'Rendimento', 'number', true),
    ],
  },
  {
    key: 'goals',
    endpoint: 'goals',
    label: 'Metas',
    description: 'Objetivos financeiros',
    columns: columns(['id', 'userId', 'title', 'amount', 'savedAmount', 'limitDate']),
    fields: [
      field('userId', 'ID do usuário', 'number'),
      field('title', 'Título'),
      field('amount', 'Valor alvo', 'number'),
      field('savedAmount', 'Valor acumulado', 'number'),
      field('limitDate', 'Data limite', 'date'),
    ],
  },
  {
    key: 'tiers',
    endpoint: 'tiers',
    label: 'Níveis',
    description: 'Níveis de relacionamento',
    columns: columns(['id', 'name', 'minPointsRequired', 'hierarchy']),
    fields: [field('name', 'Nome'), field('minPointsRequired', 'Pontos mínimos', 'number'), field('hierarchy', 'Hierarquia', 'number')],
  },
  {
    key: 'rewards',
    endpoint: 'rewards',
    label: 'Recompensas',
    description: 'Benefícios liberados aos clientes',
    columns: columns(['id', 'name', 'description', 'active']),
    fields: [field('name', 'Nome'), field('description', 'Descrição'), field('active', 'Ativa', 'boolean', false, true)],
  },
  {
    key: 'challenges',
    endpoint: 'challenges',
    label: 'Desafios',
    description: 'Campanhas de engajamento',
    columns: columns(['id', 'title', 'minTierId', 'rewardId', 'progress', 'active']),
    fields: [
      field('title', 'Título'),
      field('minTierId', 'ID do nível mínimo', 'number'),
      field('startDate', 'Início', 'date'),
      field('endDate', 'Fim', 'date'),
      field('active', 'Ativo', 'boolean', false, true),
      field('rewardId', 'ID da recompensa', 'number'),
      field('progress', 'Progresso (%)', 'number'),
    ],
  },
  {
    key: 'completedChallenges',
    endpoint: 'completed-challenges',
    label: 'Conclusões',
    description: 'Desafios concluídos pelos clientes',
    columns: columns(['id', 'userId', 'challengeId', 'completedAt']),
    fields: [field('userId', 'ID do usuário', 'number'), field('challengeId', 'ID do desafio', 'number'), field('completedAt', 'Conclusão', 'date')],
  },
]

function columns(names: string[]) {
  return names.map((name) => ({ name, label: labelFor(name) }))
}

function field(name: string, label: string, type: FieldType = 'text', optional = false, checked = false): FieldConfig {
  return { name, label, type, optional, checked }
}

function labelFor(name: string) {
  const labels: Record<string, string> = {
    id: 'ID',
    userId: 'Usuário',
    username: 'Nome',
    email: 'E-mail',
    userType: 'Tipo',
    tierId: 'Nível',
    points: 'Pontos',
    addressString: 'Endereço',
    zipCode: 'CEP',
    bank: 'Banco',
    type: 'Tipo',
    accountNumber: 'Conta',
    description: 'Descrição',
    amount: 'Valor',
    transactionDate: 'Data',
    title: 'Título',
    savedAmount: 'Guardado',
    limitDate: 'Limite',
    name: 'Nome',
    minPointsRequired: 'Pontos mín.',
    hierarchy: 'Ordem',
    active: 'Ativo',
    minTierId: 'Nível mín.',
    rewardId: 'Recompensa',
    progress: 'Progresso',
    challengeId: 'Desafio',
    completedAt: 'Conclusão',
  }
  return labels[name] ?? name
}

export function AdminDashboard() {
  const [selectedKey, setSelectedKey] = useState<AdminEntityKey>('users')
  const [data, setData] = useState<AdminEntities | null>(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const selected = entities.find((entity) => entity.key === selectedKey) ?? entities[0]
  const rows = useMemo(() => (data?.[selectedKey] ?? []) as unknown as Row[], [data, selectedKey])

  async function refresh() {
    setLoading(true)
    setError(null)
    try {
      setData(await loadAdminEntities())
    } catch (requestError) {
      setError(messageOf(requestError))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    let active = true

    loadAdminEntities()
      .then((entitiesData) => {
        if (active) {
          setData(entitiesData)
        }
      })
      .catch((requestError) => {
        if (active) {
          setError(messageOf(requestError))
        }
      })
      .finally(() => {
        if (active) {
          setLoading(false)
        }
      })

    return () => {
      active = false
    }
  }, [])

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setSaving(true)
    setError(null)
    setSuccess(null)
    const form = event.currentTarget
    const formData = new FormData(form)
    const payload: Record<string, unknown> = {}

    selected.fields.forEach((item) => {
      if (item.type === 'boolean') {
        payload[item.name] = formData.has(item.name)
        return
      }
      const value = String(formData.get(item.name) ?? '').trim()
      payload[item.name] = item.type === 'number' ? (value ? Number(value) : null) : value
    })

    try {
      await createAdminEntity(selected.endpoint, payload)
      form.reset()
      setSuccess(`${selected.label}: registro cadastrado.`)
      await refresh()
    } catch (requestError) {
      setError(messageOf(requestError))
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="space-y-5">
      <div className="flex flex-col justify-between gap-4 md:flex-row md:items-center">
        <div>
          <h2 className="flex items-center gap-2 text-xl font-semibold"><Database className="size-5 text-primary" />Administração de dados</h2>
          <p className="mt-1 text-sm text-muted-foreground">Consulte entidades do banco e cadastre novos registros.</p>
        </div>
        <Button variant="outline" onClick={() => void refresh()} disabled={loading}>
          <RefreshCw className={loading ? 'size-4 animate-spin' : 'size-4'} /> Atualizar dados
        </Button>
      </div>

      <p className="flex gap-2 rounded-xl border border-primary/15 bg-primary/5 px-4 py-3 text-sm text-primary">
        <ShieldCheck className="mt-0.5 size-4 shrink-0" />
        Área restrita a usuários ADMIN autenticados.
      </p>

      <div className="grid gap-3 sm:grid-cols-3 lg:grid-cols-5">
        {entities.map((entity) => (
          <button
            key={entity.key}
            type="button"
            onClick={() => { setSelectedKey(entity.key); setSuccess(null); setError(null) }}
            className={`rounded-xl border p-4 text-left transition ${entity.key === selectedKey ? 'border-primary bg-primary/5' : 'border-border bg-background hover:bg-muted/45'}`}
          >
            <p className="text-sm font-medium">{entity.label}</p>
            <p className="mt-2 text-2xl font-semibold">{data?.[entity.key].length ?? '-'}</p>
          </button>
        ))}
      </div>

      {error && <p role="alert" className="rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</p>}
      {success && <p className="rounded-xl bg-emerald-50 px-4 py-3 text-sm text-emerald-700">{success}</p>}

      <div className="grid gap-5 xl:grid-cols-[1.4fr_0.75fr]">
        <Card className="border-0 shadow-card">
          <CardHeader className="flex-row items-center justify-between">
            <div>
              <CardTitle>{selected.label}</CardTitle>
              <CardDescription>{selected.description}</CardDescription>
            </div>
            <Badge variant="secondary">{rows.length} registros</Badge>
          </CardHeader>
          <CardContent className="overflow-x-auto">
            {loading ? (
              <p className="flex items-center justify-center gap-2 py-12 text-sm text-muted-foreground"><LoaderCircle className="size-4 animate-spin" /> Carregando registros...</p>
            ) : rows.length === 0 ? (
              <p className="rounded-xl bg-muted/65 p-8 text-center text-sm text-muted-foreground">Nenhum registro cadastrado nesta entidade.</p>
            ) : (
              <table className="w-full min-w-[520px] text-sm">
                <thead>
                  <tr className="border-b text-left text-muted-foreground">
                    {selected.columns.map((column) => <th className="px-3 py-3 font-medium" key={column.name}>{column.label}</th>)}
                  </tr>
                </thead>
                <tbody>
                  {rows.map((row, index) => (
                    <tr key={String(row.id ?? index)} className="border-b border-border/60 last:border-0">
                      {selected.columns.map((column) => <td className="px-3 py-3" key={column.name}>{display(row[column.name])}</td>)}
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </CardContent>
        </Card>

        <Card className="border-0 shadow-card">
          <CardHeader>
            <CardTitle className="flex items-center gap-2"><Plus className="size-4 text-primary" />Cadastrar {selected.label.toLowerCase()}</CardTitle>
            <CardDescription>Os identificadores devem apontar para registros existentes.</CardDescription>
          </CardHeader>
          <CardContent>
            <form key={selected.key} className="space-y-4" onSubmit={handleSubmit}>
              {selected.fields.map((item) => (
                item.type === 'boolean' ? (
                  <label key={item.name} className="flex items-center gap-3 rounded-xl border border-border px-3 py-3 text-sm">
                    <input name={item.name} type="checkbox" defaultChecked={item.checked} className="size-4 accent-primary" />
                    {item.label}
                  </label>
                ) : (
                  <div key={item.name} className="space-y-2">
                    <Label htmlFor={`admin-${selected.key}-${item.name}`}>{item.label}</Label>
                    <Input
                      id={`admin-${selected.key}-${item.name}`}
                      name={item.name}
                      type={item.type}
                      required={!item.optional}
                      step={item.type === 'number' ? 'any' : undefined}
                    />
                  </div>
                )
              ))}
              <Button type="submit" className="w-full" disabled={saving}>
                {saving ? <LoaderCircle className="size-4 animate-spin" /> : <Plus className="size-4" />}
                {saving ? 'Salvando...' : 'Cadastrar registro'}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

function display(value: Row[string]) {
  if (typeof value === 'boolean') {
    return value ? 'Sim' : 'Não'
  }
  if (value === null || value === undefined || value === '') {
    return '-'
  }
  return String(value)
}

function messageOf(error: unknown) {
  return error instanceof Error ? error.message : 'Não foi possível concluir a operação.'
}
