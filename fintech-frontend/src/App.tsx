import { type FormEvent, useState } from 'react'
import {
  ArrowDownLeft,
  ArrowRight,
  ArrowUpRight,
  Bell,
  Building2,
  CalendarDays,
  CheckCircle2,
  CreditCard,
  Database,
  Flag,
  Gift,
  LayoutDashboard,
  LogOut,
  MapPin,
  PiggyBank,
  Plus,
  ShieldCheck,
  Sparkles,
  Target,
  Trophy,
  UserRound,
  Wallet,
} from 'lucide-react'

import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { AdminDashboard } from '@/components/admin/admin-dashboard'
import { Label } from '@/components/ui/label'
import { Progress } from '@/components/ui/progress'
import { Separator } from '@/components/ui/separator'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { createOwnBankAccount, createOwnGoal, createOwnTransaction, loadDashboard, login, logout, registerUser } from '@/api/fintech'
import type { Address, BankAccount, Challenge, CompletedChallenge, DashboardData, Goal, Reward, Tier, Transaction, User } from '@/models/fintech'

const currency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const dayMonth = new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short' })
const localDate = (date: string) => new Date(`${date}T12:00:00`)

function App() {
  const [dashboard, setDashboard] = useState<DashboardData | null>(null)
  const [authScreen, setAuthScreen] = useState<'login' | 'register'>('login')
  const [registrationComplete, setRegistrationComplete] = useState(false)

  if (!dashboard) {
    if (authScreen === 'register') {
      return (
        <Registration
          onBack={() => setAuthScreen('login')}
          onRegistered={() => {
            setRegistrationComplete(true)
            setAuthScreen('login')
          }}
        />
      )
    }
    return <Login onLogin={setDashboard} onRegister={() => setAuthScreen('register')} registrationComplete={registrationComplete} />
  }

  return <Dashboard data={dashboard} onDataChange={setDashboard} onLogout={() => { void logout(); setDashboard(null) }} />
}

function Login({
  onLogin,
  onRegister,
  registrationComplete,
}: {
  onLogin: (dashboard: DashboardData) => void
  onRegister: () => void
  registrationComplete: boolean
}) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setLoading(true)
    setError(null)

    try {
      const authentication = await login(email, password)
      const dashboard = await loadDashboard(authentication.user.id)
      onLogin(dashboard)
    } catch (loginError) {
      setError(loginError instanceof Error ? loginError.message : 'Não foi possível realizar o login.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <main className="grid min-h-screen bg-background lg:grid-cols-[1.06fr_0.94fr]">
      <section className="relative hidden overflow-hidden bg-primary px-16 py-14 text-primary-foreground lg:flex lg:flex-col">
        <div className="absolute -right-48 -top-36 size-[480px] rounded-full bg-white/10 blur-3xl" />
        <div className="absolute -bottom-44 left-28 size-[420px] rounded-full bg-teal-300/20 blur-3xl" />
        <Brand light />
        <div className="relative my-auto max-w-xl">
          <Badge className="mb-7 bg-white/15 text-white">Sua vida financeira evolui aqui</Badge>
          <h1 className="text-5xl font-semibold leading-[1.08] tracking-tight">
            Dinheiro organizado. Metas conquistadas.
          </h1>
          <p className="mt-5 max-w-md text-lg leading-relaxed text-white/70">
            Monitore contas, alcance objetivos e seja recompensado por cada decisão financeira inteligente.
          </p>
        </div>
        <div className="relative grid max-w-lg grid-cols-3 gap-3">
          {['Controle total', 'Metas reais', 'Recompensas'].map((label) => (
            <div key={label} className="rounded-2xl border border-white/10 bg-white/10 p-4 text-sm text-white/80 backdrop-blur">
              <CheckCircle2 className="mb-4 size-5 text-teal-300" />
              {label}
            </div>
          ))}
        </div>
      </section>

      <section className="flex items-center justify-center p-6 sm:p-12">
        <div className="w-full max-w-[420px]">
          <div className="mb-12 lg:hidden">
            <Brand />
          </div>
          <Badge variant="secondary" className="mb-5">Bem-vindo de volta</Badge>
          <h2 className="text-3xl font-semibold tracking-tight">Entre na FinUp</h2>
          <p className="mb-8 mt-2 text-muted-foreground">Acesse seu painel financeiro pessoal.</p>

          <form className="space-y-5" onSubmit={handleSubmit}>
            <div className="space-y-2">
              <Label htmlFor="email">E-mail</Label>
              <Input id="email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} autoComplete="email" required />
            </div>
            <div className="space-y-2">
              <div className="flex justify-between">
                <Label htmlFor="password">Senha</Label>
                <button type="button" className="text-sm font-medium text-primary hover:underline">
                  Esqueceu sua senha?
                </button>
              </div>
              <Input id="password" type="password" value={password} onChange={(event) => setPassword(event.target.value)} autoComplete="current-password" required />
            </div>
            {error && (
              <p role="alert" className="rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700">
                {error}
              </p>
            )}
            {registrationComplete && !error && (
              <p className="rounded-xl bg-emerald-50 px-4 py-3 text-sm text-emerald-700">
                Conta criada com sucesso. Entre para acessar seu painel.
              </p>
            )}
            <Button type="submit" className="w-full" size="lg" disabled={loading}>
              {loading ? 'Entrando...' : 'Entrar'} {!loading && <ArrowRight className="size-4" />}
            </Button>
          </form>
          <div className="my-8 flex items-center gap-4 text-xs uppercase tracking-widest text-muted-foreground">
            <Separator className="flex-1" /> acesso seguro <Separator className="flex-1" />
          </div>
          <p className="text-center text-sm text-muted-foreground">
            Seus dados são carregados pela API Fintech.
          </p>
          <Button variant="outline" className="mt-5 w-full" onClick={onRegister}>
            Criar minha conta
          </Button>
        </div>
      </section>
    </main>
  )
}

function Registration({ onBack, onRegistered }: { onBack: () => void; onRegistered: () => void }) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setLoading(true)
    setError(null)
    const data = new FormData(event.currentTarget)

    try {
      await registerUser({
        username: String(data.get('username')),
        email: String(data.get('email')),
        celphone: String(data.get('celphone')),
        password: String(data.get('password')),
        monthlyIncome: Number(data.get('monthlyIncome')),
        monthlySpending: Number(data.get('monthlySpending')),
      })
      onRegistered()
    } catch (registrationError) {
      setError(registrationError instanceof Error ? registrationError.message : 'Não foi possível criar sua conta.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-muted/40 p-6">
      <Card className="w-full max-w-xl border-0 shadow-card">
        <CardHeader className="space-y-5">
          <Brand />
          <div>
            <Badge variant="secondary" className="mb-4">Nova conta</Badge>
            <CardTitle className="text-3xl">Comece na FinUp</CardTitle>
            <CardDescription className="mt-2">Crie seu perfil para organizar suas finanças.</CardDescription>
          </div>
        </CardHeader>
        <CardContent>
          <form className="grid gap-5 sm:grid-cols-2" onSubmit={handleSubmit}>
            <RegistrationField name="username" label="Nome completo" className="sm:col-span-2" />
            <RegistrationField name="email" label="E-mail" type="email" />
            <RegistrationField name="celphone" label="Telefone" />
            <RegistrationField name="password" label="Senha" type="password" />
            <RegistrationField name="monthlyIncome" label="Renda mensal" type="number" />
            <RegistrationField name="monthlySpending" label="Gastos mensais" type="number" />
            {error && <p role="alert" className="rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700 sm:col-span-2">{error}</p>}
            <div className="flex flex-col-reverse gap-3 pt-2 sm:col-span-2 sm:flex-row">
              <Button type="button" variant="outline" className="flex-1" onClick={onBack}>Voltar para login</Button>
              <Button type="submit" className="flex-1" disabled={loading}>
                {loading ? 'Criando conta...' : 'Criar conta'} {!loading && <ArrowRight className="size-4" />}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </main>
  )
}

function RegistrationField({ name, label, type = 'text', className }: { name: string; label: string; type?: string; className?: string }) {
  return (
    <div className={`space-y-2 ${className ?? ''}`}>
      <Label htmlFor={`register-${name}`}>{label}</Label>
      <Input id={`register-${name}`} name={name} type={type} required min={type === 'number' ? 0 : undefined} step={type === 'number' ? 'any' : undefined} />
    </div>
  )
}

function Brand({ light = false }: { light?: boolean }) {
  return (
    <div className="relative flex items-center gap-3">
      <span className={light ? 'brand-mark-light' : 'brand-mark'}>
        <Sparkles className="size-5" />
      </span>
      <span className="text-2xl font-semibold tracking-tight">FinUp</span>
    </div>
  )
}

function Dashboard({
  data,
  onDataChange,
  onLogout,
}: {
  data: DashboardData
  onDataChange: (dashboard: DashboardData) => void
  onLogout: () => void
}) {
  const [showTransactionForm, setShowTransactionForm] = useState(false)
  const { user, address, accounts, transactions, goals, tiers, rewards, challenges, completedChallenges } = data
  const currentTier = tiers.find((tier) => tier.id === user.tierId)
  const nextTier = currentTier ? tiers.find((tier) => tier.hierarchy === currentTier.hierarchy + 1) : undefined
  const balance = data.summary.balance
  const pointsProgress = nextTier ? (user.points / nextTier.minPointsRequired) * 100 : 100
  const pointsRemaining = nextTier ? Math.max(nextTier.minPointsRequired - user.points, 0) : 0
  const tierName = currentTier?.name ?? 'Sem nível'
  const activeChallenges = challenges.filter((challenge) => challenge.active)
  const activeRewards = rewards.filter((reward) => reward.active)
  const firstName = user.username.split(' ')[0] || user.username
  const isAdmin = user.userType === 'ADMIN'
  const initials = user.username
    .split(' ')
    .slice(0, 2)
    .map((part) => part.charAt(0))
    .join('')
    .toUpperCase()

  async function refreshDashboard() {
    onDataChange(await loadDashboard(user.id))
  }

  return (
    <div className="min-h-screen bg-muted/40">
      <header className="sticky top-0 z-10 border-b border-border bg-background/90 backdrop-blur">
        <div className="mx-auto flex h-18 max-w-7xl items-center justify-between px-5 lg:px-8">
          <Brand />
          <div className="flex items-center gap-2 sm:gap-4">
            <Button variant="ghost" size="icon" aria-label="Notificações">
              <Bell className="size-5" />
            </Button>
            <Separator orientation="vertical" className="hidden h-8 sm:block" />
            <Avatar>
              <AvatarFallback>{initials}</AvatarFallback>
            </Avatar>
            <div className="hidden sm:block">
              <p className="text-sm font-medium">{user.username}</p>
              <p className="text-xs text-muted-foreground">{tierName}</p>
            </div>
            <Button variant="ghost" size="icon" onClick={onLogout} aria-label="Sair">
              <LogOut className="size-5" />
            </Button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-5 py-7 lg:px-8">
        <div className="mb-7 flex flex-col justify-between gap-4 md:flex-row md:items-end">
          <div>
            <p className="text-sm text-muted-foreground">Olá, {firstName}</p>
            <h1 className="mt-1 text-3xl font-semibold tracking-tight">Seu painel financeiro</h1>
          </div>
          <Button onClick={() => setShowTransactionForm((visible) => !visible)}>
            <Plus className="size-4" /> Nova transação
          </Button>
        </div>

        {showTransactionForm && (
          <TransactionForm
            accounts={accounts}
            goals={goals}
            onCancel={() => setShowTransactionForm(false)}
            onCreate={async (transaction) => {
              await createOwnTransaction(transaction)
              await refreshDashboard()
              setShowTransactionForm(false)
            }}
          />
        )}

        <section className="mb-7 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          <MetricCard title="Saldo disponível" value={currency.format(balance)} icon={Wallet} trend="Receitas − despesas − investimentos" />
          <MetricCard title="Receitas" value={currency.format(data.summary.totalIncome)} icon={ArrowDownLeft} trend="Total de entradas registradas" />
          <MetricCard title="Despesas" value={currency.format(data.summary.totalExpense)} icon={CreditCard} trend="Total de saídas registradas" />
          <MetricCard title="Investido" value={currency.format(data.summary.totalInvested)} icon={PiggyBank} trend={`${user.points.toLocaleString('pt-BR')} pts • ${tierName}`} />
        </section>

        <Tabs defaultValue="resumo">
          <TabsList className="w-full justify-start overflow-x-auto sm:w-auto">
            <TabsTrigger value="resumo"><LayoutDashboard className="mr-2 size-4" />Resumo</TabsTrigger>
            <TabsTrigger value="financas"><Wallet className="mr-2 size-4" />Finanças</TabsTrigger>
            <TabsTrigger value="conquistas"><Trophy className="mr-2 size-4" />Conquistas</TabsTrigger>
            <TabsTrigger value="perfil"><UserRound className="mr-2 size-4" />Perfil</TabsTrigger>
            {isAdmin && <TabsTrigger value="admin"><Database className="mr-2 size-4" />Admin</TabsTrigger>}
          </TabsList>

          <TabsContent value="resumo">
            <div className="grid gap-5 xl:grid-cols-[1.08fr_0.92fr]">
              <TransactionsCard transactions={transactions} compact />
              <div className="grid gap-5">
                <GoalsCard goals={goals} compact />
                <ChallengeHighlight challenges={activeChallenges} />
              </div>
            </div>
          </TabsContent>

          <TabsContent value="financas">
            <div className="grid gap-5 xl:grid-cols-[0.8fr_1.2fr]">
              <div className="grid gap-5">
                <AccountsCard
                  accounts={accounts}
                  onCreate={async (account) => {
                    await createOwnBankAccount(account)
                    await refreshDashboard()
                  }}
                />
                <GoalsCard
                  goals={goals}
                  onCreate={async (goal) => {
                    await createOwnGoal(goal)
                    await refreshDashboard()
                  }}
                />
              </div>
              <TransactionsCard transactions={transactions} />
            </div>
          </TabsContent>

          <TabsContent value="conquistas">
            <Achievements
              currentTier={tierName}
              nextTier={nextTier?.name ?? 'Máximo'}
              pointsProgress={pointsProgress}
              pointsRemaining={pointsRemaining}
              points={user.points}
              challenges={activeChallenges}
              completedChallenges={completedChallenges}
              rewards={activeRewards}
            />
          </TabsContent>

          <TabsContent value="perfil">
            <Profile data={{ user, address, accounts, transactions, goals, tiers, rewards: activeRewards, challenges: activeChallenges }} />
          </TabsContent>

          {isAdmin && (
            <TabsContent value="admin">
              <AdminDashboard />
            </TabsContent>
          )}
        </Tabs>
      </main>
    </div>
  )
}

function TransactionForm({
  accounts,
  goals,
  onCreate,
  onCancel,
}: {
  accounts: BankAccount[]
  goals: Goal[]
  onCreate: (transaction: {
    amount: number
    type: 'Receita' | 'Despesa' | 'Investimento'
    description: string
    transactionDate: string
    bankAccountId: number
    yield: number | null
    goalId: number | null
  }) => Promise<void>
  onCancel: () => void
}) {
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [type, setType] = useState<'Receita' | 'Despesa' | 'Investimento'>('Receita')

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    const yieldValue = String(data.get('yield') ?? '').trim()
    const goalValue = String(data.get('goalId') ?? '').trim()
    setSaving(true)
    setError(null)
    try {
      await onCreate({
        amount: Number(data.get('amount')),
        type: String(data.get('type')) as 'Receita' | 'Despesa' | 'Investimento',
        description: String(data.get('description')),
        transactionDate: String(data.get('transactionDate')),
        bankAccountId: Number(data.get('bankAccountId')),
        yield: yieldValue ? Number(yieldValue) : null,
        goalId: goalValue ? Number(goalValue) : null,
      })
    } catch (requestError) {
      setError(requestError instanceof Error ? requestError.message : 'Não foi possível cadastrar a transação.')
      setSaving(false)
    }
  }

  return (
    <Card className="mb-7 border-0 shadow-card">
      <CardHeader className="flex-row items-start justify-between">
        <div>
          <CardTitle>Nova transação</CardTitle>
          <CardDescription>Registre uma movimentação em uma de suas contas bancárias.</CardDescription>
        </div>
        <Button variant="ghost" size="sm" onClick={onCancel}>Cancelar</Button>
      </CardHeader>
      <CardContent>
        {accounts.length === 0 ? (
          <EmptyState message="Cadastre uma conta bancária antes de registrar transações." />
        ) : (
          <form className="grid gap-4 md:grid-cols-3" onSubmit={handleSubmit}>
            <div className="space-y-2">
              <Label htmlFor="transaction-description">Descrição</Label>
              <Input id="transaction-description" name="description" placeholder="Ex.: Supermercado" required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="transaction-type">Tipo</Label>
              <select
                id="transaction-type"
                name="type"
                value={type}
                onChange={(event) => setType(event.target.value as 'Receita' | 'Despesa' | 'Investimento')}
                className="flex h-11 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none focus-visible:ring-2 focus-visible:ring-ring"
                required
              >
                <option value="Receita">Receita</option>
                <option value="Despesa">Despesa</option>
                <option value="Investimento">Investimento</option>
              </select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="transaction-account">Conta bancária</Label>
              <select
                id="transaction-account"
                name="bankAccountId"
                className="flex h-11 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none focus-visible:ring-2 focus-visible:ring-ring"
                required
              >
                {accounts.map((account) => (
                  <option key={account.id} value={account.id}>{account.bank} - {account.description}</option>
                ))}
              </select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="transaction-amount">Valor</Label>
              <Input id="transaction-amount" name="amount" type="number" min="0.01" step="0.01" required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="transaction-date">Data</Label>
              <Input id="transaction-date" name="transactionDate" type="date" required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="transaction-yield">Rendimento (opcional)</Label>
              <Input id="transaction-yield" name="yield" type="number" step="1" min="0" placeholder="Para investimentos" />
            </div>
            {type === 'Investimento' && (
              <div className="space-y-2">
                <Label htmlFor="transaction-goal">Meta (opcional)</Label>
                <select
                  id="transaction-goal"
                  name="goalId"
                  className="flex h-11 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none focus-visible:ring-2 focus-visible:ring-ring"
                >
                  <option value="">Não vincular a uma meta</option>
                  {goals.map((goal) => (
                    <option key={goal.id} value={goal.id}>{goal.title}</option>
                  ))}
                </select>
              </div>
            )}
            {error && <p role="alert" className="rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700 md:col-span-3">{error}</p>}
            <Button type="submit" disabled={saving} className="md:col-span-3 md:justify-self-end">
              {saving ? 'Salvando...' : 'Salvar transação'}
            </Button>
          </form>
        )}
      </CardContent>
    </Card>
  )
}

function MetricCard({ title, value, icon: Icon, trend }: { title: string; value: string; icon: typeof Wallet; trend: string }) {
  return (
    <Card className="border-0 shadow-card">
      <CardContent className="p-5">
        <div className="mb-5 flex justify-between">
          <p className="text-sm text-muted-foreground">{title}</p>
          <span className="rounded-xl bg-primary/8 p-2 text-primary"><Icon className="size-5" /></span>
        </div>
        <p className="text-2xl font-semibold tracking-tight">{value}</p>
        <p className="mt-2 text-xs text-muted-foreground">{trend}</p>
      </CardContent>
    </Card>
  )
}

function TransactionsCard({ transactions, compact = false }: { transactions: Transaction[]; compact?: boolean }) {
  const visible = compact ? transactions.slice(0, 3) : transactions

  return (
    <Card className="border-0 shadow-card">
      <CardHeader className="flex-row items-center justify-between">
        <div>
          <CardTitle>Transações recentes</CardTitle>
          <CardDescription>Movimentações das suas contas</CardDescription>
        </div>
        <Badge variant="secondary">{transactions.length} lancamentos</Badge>
      </CardHeader>
      <CardContent className="space-y-1">
        {visible.length === 0 && <EmptyState message="Nenhuma transação encontrada." />}
        {visible.map((transaction) => {
          const outgoing = transaction.type !== 'Receita'
          return (
            <div key={transaction.id} className="flex items-center gap-4 rounded-xl px-2 py-3 hover:bg-muted/60">
              <span className={outgoing ? 'transaction-out' : 'transaction-in'}>
                {outgoing ? <ArrowUpRight className="size-4" /> : <ArrowDownLeft className="size-4" />}
              </span>
              <div className="min-w-0 flex-1">
                <p className="truncate text-sm font-medium">{transaction.description}</p>
                <p className="text-xs text-muted-foreground">{transaction.type} • {dayMonth.format(localDate(transaction.transactionDate))}</p>
              </div>
              <p className={outgoing ? 'text-sm font-semibold' : 'text-sm font-semibold text-emerald-600'}>
                {outgoing ? '- ' : '+ '}{currency.format(transaction.amount)}
              </p>
            </div>
          )
        })}
      </CardContent>
    </Card>
  )
}

function GoalsCard({
  goals,
  compact = false,
  onCreate,
}: {
  goals: Goal[]
  compact?: boolean
  onCreate?: (goal: { title: string; amount: number; limitDate: string }) => Promise<void>
}) {
  const [saving, setSaving] = useState(false)
  const [feedback, setFeedback] = useState<{ type: 'error' | 'success'; message: string } | null>(null)

  async function handleCreate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (!onCreate) {
      return
    }
    const form = event.currentTarget
    const data = new FormData(form)
    setSaving(true)
    setFeedback(null)
    try {
      await onCreate({
        title: String(data.get('title')),
        amount: Number(data.get('amount')),
        limitDate: String(data.get('limitDate')),
      })
      form.reset()
      setFeedback({ type: 'success', message: 'Meta cadastrada com sucesso.' })
    } catch (error) {
      setFeedback({ type: 'error', message: error instanceof Error ? error.message : 'Não foi possível cadastrar a meta.' })
    } finally {
      setSaving(false)
    }
  }

  return (
    <Card className="border-0 shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2"><Target className="size-5 text-primary" />Metas financeiras</CardTitle>
        {!compact && <CardDescription>Acompanhe seus objetivos até a data limite</CardDescription>}
      </CardHeader>
      <CardContent className="space-y-5">
        {goals.length === 0 && <EmptyState message="Nenhuma meta cadastrada." />}
        {goals.map((goal) => {
          const progress = Math.round((goal.savedAmount / goal.amount) * 100)
          return (
            <div key={goal.id}>
              <div className="mb-2 flex justify-between text-sm">
                <span className="font-medium">{goal.title}</span>
                <span className="text-muted-foreground">{progress}%</span>
              </div>
              <Progress value={progress} />
              {!compact && <p className="mt-2 text-xs text-muted-foreground">{currency.format(goal.savedAmount)} de {currency.format(goal.amount)} • até {dayMonth.format(localDate(goal.limitDate))}</p>}
            </div>
          )
        })}
        {onCreate && (
          <>
            <Separator />
            <form className="space-y-3 pt-1" onSubmit={handleCreate}>
              <p className="text-sm font-medium">Nova meta</p>
              <Input name="title" placeholder="Ex.: Reserva de emergencia" required />
              <div className="grid grid-cols-2 gap-3">
                <Input name="amount" type="number" min="0.01" step="0.01" placeholder="Valor alvo" required />
                <Input name="limitDate" type="date" required />
              </div>
              {feedback && <FormFeedback feedback={feedback} />}
              <Button type="submit" className="w-full" disabled={saving}>
                <Plus className="size-4" /> {saving ? 'Salvando...' : 'Cadastrar meta'}
              </Button>
            </form>
          </>
        )}
      </CardContent>
    </Card>
  )
}

function ChallengeHighlight({ challenges }: { challenges: Challenge[] }) {
  const challenge = challenges[0]

  if (!challenge) {
    return (
      <Card className="border-0 shadow-card">
        <CardContent className="p-5">
          <EmptyState message="Nenhum desafio disponível no momento." />
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="overflow-hidden border-0 bg-primary text-primary-foreground shadow-card">
      <CardContent className="flex items-center gap-5 p-5">
        <div className="hidden rounded-2xl bg-white/12 p-4 sm:block"><Flag className="size-7" /></div>
        <div className="flex-1">
          <p className="text-xs uppercase tracking-widest text-white/65">Desafio em destaque</p>
          <p className="mt-1 font-semibold">{challenge.title}</p>
          <Progress className="mt-3 bg-white/20 [&>div]:bg-white" value={challenge.progress} />
          <p className="mt-2 text-xs text-white/70">{challenge.progress}% concluído • recompensa disponível</p>
        </div>
      </CardContent>
    </Card>
  )
}

function AccountsCard({
  accounts,
  onCreate,
}: {
  accounts: BankAccount[]
  onCreate?: (account: { bank: string; type: string; description: string; agency: string; accountNumber: string }) => Promise<void>
}) {
  const [saving, setSaving] = useState(false)
  const [feedback, setFeedback] = useState<{ type: 'error' | 'success'; message: string } | null>(null)

  async function handleCreate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (!onCreate) {
      return
    }
    const form = event.currentTarget
    const data = new FormData(form)
    setSaving(true)
    setFeedback(null)
    try {
      await onCreate({
        bank: String(data.get('bank')),
        type: String(data.get('type')),
        description: String(data.get('description')),
        agency: String(data.get('agency')),
        accountNumber: String(data.get('accountNumber')),
      })
      form.reset()
      setFeedback({ type: 'success', message: 'Conta cadastrada com sucesso.' })
    } catch (error) {
      setFeedback({ type: 'error', message: error instanceof Error ? error.message : 'Não foi possível cadastrar a conta.' })
    } finally {
      setSaving(false)
    }
  }

  return (
    <Card className="border-0 shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2"><Building2 className="size-5 text-primary" />Contas bancárias</CardTitle>
        <CardDescription>Contas cadastradas e vinculadas</CardDescription>
      </CardHeader>
      <CardContent className="space-y-3">
        {accounts.length === 0 && <EmptyState message="Nenhuma conta bancária vinculada." />}
        {accounts.map((account) => (
          <div key={account.id} className="rounded-xl border border-border p-4">
            <div className="flex justify-between">
              <p className="font-medium">{account.bank}</p>
              <Badge variant="secondary">{account.type}</Badge>
            </div>
            <p className="mt-1 text-sm text-muted-foreground">{account.description}</p>
            <p className="mt-3 text-xs text-muted-foreground">Ag. {account.agency} • Conta {account.accountNumber}</p>
          </div>
        ))}
        {onCreate && (
          <>
            <Separator />
            <form className="space-y-3 pt-1" onSubmit={handleCreate}>
              <p className="text-sm font-medium">Nova conta bancária</p>
              <div className="grid grid-cols-2 gap-3">
                <Input name="bank" placeholder="Banco" required />
                <Input name="type" placeholder="Tipo da conta" required />
              </div>
              <Input name="description" placeholder="Descrição" required />
              <div className="grid grid-cols-2 gap-3">
                <Input name="agency" placeholder="Agência" required />
                <Input name="accountNumber" placeholder="Numero da conta" required />
              </div>
              {feedback && <FormFeedback feedback={feedback} />}
              <Button type="submit" className="w-full" disabled={saving}>
                <Plus className="size-4" /> {saving ? 'Salvando...' : 'Cadastrar conta'}
              </Button>
            </form>
          </>
        )}
      </CardContent>
    </Card>
  )
}

function Achievements({
  currentTier,
  nextTier,
  pointsProgress,
  pointsRemaining,
  points,
  challenges,
  completedChallenges,
  rewards,
}: {
  currentTier: string
  nextTier: string
  pointsProgress: number
  pointsRemaining: number
  points: number
  challenges: Challenge[]
  completedChallenges: CompletedChallenge[]
  rewards: Reward[]
}) {
  return (
    <div className="grid gap-5 lg:grid-cols-3">
      <Card className="border-0 shadow-card lg:col-span-3">
        <CardContent className="grid gap-5 p-6 md:grid-cols-[1fr_auto] md:items-center">
          <div>
            <Badge>Programa de níveis</Badge>
            <h2 className="mt-3 text-xl font-semibold">Nível {currentTier} • {points.toLocaleString('pt-BR')} pontos</h2>
            <p className="mt-1 text-sm text-muted-foreground">Faltam {pointsRemaining} pontos para chegar ao nível {nextTier}.</p>
            <Progress value={pointsProgress} className="mt-5 max-w-lg" />
          </div>
          <Trophy className="size-16 text-primary/25" />
        </CardContent>
      </Card>
      <Card className="border-0 shadow-card lg:col-span-2">
        <CardHeader>
          <CardTitle>Desafios ativos</CardTitle>
          <CardDescription>Complete desafios para liberar recompensas</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          {challenges.length === 0 && <EmptyState message="Nenhum desafio ativo." />}
          {challenges.map((challenge) => (
            <div key={challenge.id} className="rounded-xl border border-border p-4">
              <div className="mb-3 flex justify-between gap-3">
                <p className="font-medium">{challenge.title}</p>
                <Badge variant="success">Ativo</Badge>
              </div>
              <Progress value={challenge.progress} />
              <p className="mt-3 text-xs text-muted-foreground">{challenge.progress}% concluído • termina em {dayMonth.format(localDate(challenge.endDate))}</p>
            </div>
          ))}
          <p className="flex items-center gap-2 pt-2 text-sm text-muted-foreground">
            <CheckCircle2 className="size-4 text-emerald-600" /> {completedChallenges.length} {completedChallenges.length === 1 ? 'desafio concluído' : 'desafios concluídos'} recentemente
          </p>
        </CardContent>
      </Card>
      <Card className="border-0 shadow-card">
        <CardHeader>
          <CardTitle className="flex items-center gap-2"><Gift className="size-5 text-primary" />Recompensas</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {rewards.length === 0 && <EmptyState message="Nenhuma recompensa disponível." />}
          {rewards.map((reward) => (
            <div key={reward.id}>
              <p className="text-sm font-medium">{reward.name}</p>
              <p className="mt-1 text-xs leading-relaxed text-muted-foreground">{reward.description}</p>
            </div>
          ))}
        </CardContent>
      </Card>
    </div>
  )
}

function Profile({
  data,
}: {
  data: {
    user: User
    address: Address | null
    accounts: BankAccount[]
    transactions: Transaction[]
    goals: Goal[]
    tiers: Tier[]
    rewards: Reward[]
    challenges: Challenge[]
  }
}) {
  const { user, address, accounts, transactions, goals, tiers, rewards, challenges } = data
  const initials = user.username
    .split(' ')
    .slice(0, 2)
    .map((part) => part.charAt(0))
    .join('')
    .toUpperCase()

  return (
    <div className="grid gap-5 lg:grid-cols-[0.9fr_1.1fr]">
      <Card className="border-0 shadow-card">
        <CardContent className="p-6">
          <Avatar className="mb-4 size-16"><AvatarFallback className="text-xl">{initials}</AvatarFallback></Avatar>
          <h2 className="text-xl font-semibold">{user.username}</h2>
          <p className="text-sm text-muted-foreground">{user.email}</p>
          <Separator className="my-6" />
          <div className="space-y-4 text-sm">
            <InfoRow icon={ShieldCheck} label="Nível atual" value={tiers.find((tier) => tier.id === user.tierId)?.name ?? '-'} />
            <InfoRow icon={CreditCard} label="Telefone" value={user.celphone} />
            <InfoRow icon={MapPin} label="Endereço" value={address?.addressString ?? 'Não informado'} />
          </div>
        </CardContent>
      </Card>
      <Card className="border-0 shadow-card">
        <CardHeader>
          <CardTitle>Recursos da conta</CardTitle>
          <CardDescription>Dados mapeados a partir dos modelos do backend</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-3 sm:grid-cols-2">
          {[
            [Wallet, 'Contas bancárias', `${accounts.length} vinculadas`],
            [ArrowUpRight, 'Transações', `${transactions.length} recentes`],
            [PiggyBank, 'Metas', `${goals.length} em andamento`],
            [Trophy, 'Desafios', `${challenges.length} ativos`],
            [Gift, 'Recompensas', `${rewards.length} disponíveis`],
            [CalendarDays, 'Endereço principal', address?.zipCode ?? 'Não informado'],
          ].map(([Icon, label, value]) => {
            const ResourceIcon = Icon as typeof Wallet
            return (
              <div key={label as string} className="rounded-xl bg-muted/65 p-4">
                <ResourceIcon className="mb-3 size-5 text-primary" />
                <p className="text-sm font-medium">{label as string}</p>
                <p className="text-xs text-muted-foreground">{value as string}</p>
              </div>
            )
          })}
        </CardContent>
      </Card>
    </div>
  )
}

function InfoRow({ icon: Icon, label, value }: { icon: typeof Wallet; label: string; value: string }) {
  return (
    <div className="flex gap-3">
      <Icon className="mt-0.5 size-4 shrink-0 text-primary" />
      <div>
        <p className="text-xs text-muted-foreground">{label}</p>
        <p>{value}</p>
      </div>
    </div>
  )
}

function EmptyState({ message }: { message: string }) {
  return <p className="rounded-xl bg-muted/65 px-4 py-5 text-center text-sm text-muted-foreground">{message}</p>
}

function FormFeedback({ feedback }: { feedback: { type: 'error' | 'success'; message: string } }) {
  return (
    <p className={feedback.type === 'error' ? 'rounded-xl bg-red-50 px-3 py-2 text-sm text-red-700' : 'rounded-xl bg-emerald-50 px-3 py-2 text-sm text-emerald-700'}>
      {feedback.message}
    </p>
  )
}

export default App
