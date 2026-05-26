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
import { Label } from '@/components/ui/label'
import { Progress } from '@/components/ui/progress'
import { Separator } from '@/components/ui/separator'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { accounts, address, challenges, completedChallenges, goals, rewards, tiers, transactions, user } from '@/data/demo'

const currency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const dayMonth = new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short' })
const localDate = (date: string) => new Date(`${date}T12:00:00`)

function App() {
  const [authenticated, setAuthenticated] = useState(false)

  if (!authenticated) {
    return <Login onLogin={() => setAuthenticated(true)} />
  }

  return <Dashboard onLogout={() => setAuthenticated(false)} />
}

function Login({ onLogin }: { onLogin: () => void }) {
  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    onLogin()
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
            Monitore contas, alcance objetivos e seja recompensado por cada decisao financeira inteligente.
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
              <Input id="email" type="email" defaultValue="ricardo@finup.com" required />
            </div>
            <div className="space-y-2">
              <div className="flex justify-between">
                <Label htmlFor="password">Senha</Label>
                <button type="button" className="text-sm font-medium text-primary hover:underline">
                  Esqueceu sua senha?
                </button>
              </div>
              <Input id="password" type="password" defaultValue="finup-demo" required />
            </div>
            <Button type="submit" className="w-full" size="lg">
              Entrar <ArrowRight className="size-4" />
            </Button>
          </form>
          <div className="my-8 flex items-center gap-4 text-xs uppercase tracking-widest text-muted-foreground">
            <Separator className="flex-1" /> acesso seguro <Separator className="flex-1" />
          </div>
          <p className="text-center text-sm text-muted-foreground">
            Demonstracao visual conectavel ao backend Fintech.
          </p>
        </div>
      </section>
    </main>
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

function Dashboard({ onLogout }: { onLogout: () => void }) {
  const currentTier = tiers.find((tier) => tier.id === user.tierId) ?? tiers[0]
  const nextTier = tiers.find((tier) => tier.hierarchy === currentTier.hierarchy + 1)
  const balance = user.monthlyIncome - user.monthlySpending
  const pointsProgress = nextTier ? (user.points / nextTier.minPointsRequired) * 100 : 100
  const pointsRemaining = nextTier ? Math.max(nextTier.minPointsRequired - user.points, 0) : 0

  return (
    <div className="min-h-screen bg-muted/40">
      <header className="sticky top-0 z-10 border-b border-border bg-background/90 backdrop-blur">
        <div className="mx-auto flex h-18 max-w-7xl items-center justify-between px-5 lg:px-8">
          <Brand />
          <div className="flex items-center gap-2 sm:gap-4">
            <Button variant="ghost" size="icon" aria-label="Notificacoes">
              <Bell className="size-5" />
            </Button>
            <Separator orientation="vertical" className="hidden h-8 sm:block" />
            <Avatar>
              <AvatarFallback>RA</AvatarFallback>
            </Avatar>
            <div className="hidden sm:block">
              <p className="text-sm font-medium">{user.username}</p>
              <p className="text-xs text-muted-foreground">{currentTier.name}</p>
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
            <p className="text-sm text-muted-foreground">Boa noite, Ricardo</p>
            <h1 className="mt-1 text-3xl font-semibold tracking-tight">Seu painel financeiro</h1>
          </div>
          <Button>
            <Plus className="size-4" /> Nova transacao
          </Button>
        </div>

        <section className="mb-7 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          <MetricCard title="Saldo disponivel" value={currency.format(balance)} icon={Wallet} trend="+12,4% este mes" />
          <MetricCard title="Receita mensal" value={currency.format(user.monthlyIncome)} icon={ArrowDownLeft} trend="Receita registrada" />
          <MetricCard title="Gastos mensais" value={currency.format(user.monthlySpending)} icon={CreditCard} trend="59,8% da renda" />
          <MetricCard title="Pontos acumulados" value={`${user.points.toLocaleString('pt-BR')} pts`} icon={Trophy} trend={`Nivel ${currentTier.name}`} />
        </section>

        <Tabs defaultValue="resumo">
          <TabsList className="w-full justify-start overflow-x-auto sm:w-auto">
            <TabsTrigger value="resumo"><LayoutDashboard className="mr-2 size-4" />Resumo</TabsTrigger>
            <TabsTrigger value="financas"><Wallet className="mr-2 size-4" />Financas</TabsTrigger>
            <TabsTrigger value="conquistas"><Trophy className="mr-2 size-4" />Conquistas</TabsTrigger>
            <TabsTrigger value="perfil"><UserRound className="mr-2 size-4" />Perfil</TabsTrigger>
          </TabsList>

          <TabsContent value="resumo">
            <div className="grid gap-5 xl:grid-cols-[1.08fr_0.92fr]">
              <TransactionsCard compact />
              <div className="grid gap-5">
                <GoalsCard compact />
                <ChallengeHighlight />
              </div>
            </div>
          </TabsContent>

          <TabsContent value="financas">
            <div className="grid gap-5 xl:grid-cols-[0.8fr_1.2fr]">
              <div className="grid gap-5">
                <AccountsCard />
                <GoalsCard />
              </div>
              <TransactionsCard />
            </div>
          </TabsContent>

          <TabsContent value="conquistas">
            <Achievements
              currentTier={currentTier.name}
              nextTier={nextTier?.name ?? 'Maximo'}
              pointsProgress={pointsProgress}
              pointsRemaining={pointsRemaining}
            />
          </TabsContent>

          <TabsContent value="perfil">
            <Profile />
          </TabsContent>
        </Tabs>
      </main>
    </div>
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

function TransactionsCard({ compact = false }: { compact?: boolean }) {
  const visible = compact ? transactions.slice(0, 3) : transactions

  return (
    <Card className="border-0 shadow-card">
      <CardHeader className="flex-row items-center justify-between">
        <div>
          <CardTitle>Transacoes recentes</CardTitle>
          <CardDescription>Movimentacoes das suas contas</CardDescription>
        </div>
        <Badge variant="secondary">{transactions.length} lancamentos</Badge>
      </CardHeader>
      <CardContent className="space-y-1">
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

function GoalsCard({ compact = false }: { compact?: boolean }) {
  return (
    <Card className="border-0 shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2"><Target className="size-5 text-primary" />Metas financeiras</CardTitle>
        {!compact && <CardDescription>Acompanhe seus objetivos ate a data limite</CardDescription>}
      </CardHeader>
      <CardContent className="space-y-5">
        {goals.map((goal) => {
          const progress = Math.round((goal.savedAmount / goal.amount) * 100)
          return (
            <div key={goal.id}>
              <div className="mb-2 flex justify-between text-sm">
                <span className="font-medium">{goal.title}</span>
                <span className="text-muted-foreground">{progress}%</span>
              </div>
              <Progress value={progress} />
              {!compact && <p className="mt-2 text-xs text-muted-foreground">{currency.format(goal.savedAmount)} de {currency.format(goal.amount)} • ate {dayMonth.format(localDate(goal.limitDate))}</p>}
            </div>
          )
        })}
      </CardContent>
    </Card>
  )
}

function ChallengeHighlight() {
  const challenge = challenges[0]

  return (
    <Card className="overflow-hidden border-0 bg-primary text-primary-foreground shadow-card">
      <CardContent className="flex items-center gap-5 p-5">
        <div className="hidden rounded-2xl bg-white/12 p-4 sm:block"><Flag className="size-7" /></div>
        <div className="flex-1">
          <p className="text-xs uppercase tracking-widest text-white/65">Desafio em destaque</p>
          <p className="mt-1 font-semibold">{challenge.title}</p>
          <Progress className="mt-3 bg-white/20 [&>div]:bg-white" value={challenge.progress} />
          <p className="mt-2 text-xs text-white/70">{challenge.progress}% concluido • recompensa disponivel</p>
        </div>
      </CardContent>
    </Card>
  )
}

function AccountsCard() {
  return (
    <Card className="border-0 shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2"><Building2 className="size-5 text-primary" />Contas bancarias</CardTitle>
        <CardDescription>Contas cadastradas e vinculadas</CardDescription>
      </CardHeader>
      <CardContent className="space-y-3">
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
      </CardContent>
    </Card>
  )
}

function Achievements({
  currentTier,
  nextTier,
  pointsProgress,
  pointsRemaining,
}: {
  currentTier: string
  nextTier: string
  pointsProgress: number
  pointsRemaining: number
}) {
  return (
    <div className="grid gap-5 lg:grid-cols-3">
      <Card className="border-0 shadow-card lg:col-span-3">
        <CardContent className="grid gap-5 p-6 md:grid-cols-[1fr_auto] md:items-center">
          <div>
            <Badge>Programa de niveis</Badge>
            <h2 className="mt-3 text-xl font-semibold">Nivel {currentTier} • {user.points.toLocaleString('pt-BR')} pontos</h2>
            <p className="mt-1 text-sm text-muted-foreground">Faltam {pointsRemaining} pontos para chegar ao nivel {nextTier}.</p>
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
          {challenges.map((challenge) => (
            <div key={challenge.id} className="rounded-xl border border-border p-4">
              <div className="mb-3 flex justify-between gap-3">
                <p className="font-medium">{challenge.title}</p>
                <Badge variant="success">Ativo</Badge>
              </div>
              <Progress value={challenge.progress} />
              <p className="mt-3 text-xs text-muted-foreground">{challenge.progress}% concluido • termina em {dayMonth.format(localDate(challenge.endDate))}</p>
            </div>
          ))}
          <p className="flex items-center gap-2 pt-2 text-sm text-muted-foreground">
            <CheckCircle2 className="size-4 text-emerald-600" /> {completedChallenges.length} desafio concluido recentemente
          </p>
        </CardContent>
      </Card>
      <Card className="border-0 shadow-card">
        <CardHeader>
          <CardTitle className="flex items-center gap-2"><Gift className="size-5 text-primary" />Recompensas</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
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

function Profile() {
  return (
    <div className="grid gap-5 lg:grid-cols-[0.9fr_1.1fr]">
      <Card className="border-0 shadow-card">
        <CardContent className="p-6">
          <Avatar className="mb-4 size-16"><AvatarFallback className="text-xl">RA</AvatarFallback></Avatar>
          <h2 className="text-xl font-semibold">{user.username}</h2>
          <p className="text-sm text-muted-foreground">{user.email}</p>
          <Separator className="my-6" />
          <div className="space-y-4 text-sm">
            <InfoRow icon={ShieldCheck} label="Nivel atual" value={tiers.find((tier) => tier.id === user.tierId)?.name ?? '-'} />
            <InfoRow icon={CreditCard} label="Telefone" value={user.celphone} />
            <InfoRow icon={MapPin} label="Endereco" value={address.addressString} />
          </div>
        </CardContent>
      </Card>
      <Card className="border-0 shadow-card">
        <CardHeader>
          <CardTitle>Recursos da conta</CardTitle>
          <CardDescription>Dados mapeados a partir dos models do backend</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-3 sm:grid-cols-2">
          {[
            [Wallet, 'Contas bancarias', `${accounts.length} vinculadas`],
            [ArrowUpRight, 'Transacoes', `${transactions.length} recentes`],
            [PiggyBank, 'Metas', `${goals.length} em andamento`],
            [Trophy, 'Desafios', `${challenges.length} ativos`],
            [Gift, 'Recompensas', `${rewards.length} disponiveis`],
            [CalendarDays, 'Endereco principal', address.zipCode],
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

export default App
