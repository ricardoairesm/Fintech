import type { Address, BankAccount, Challenge, CompletedChallenge, Goal, Reward, Tier, Transaction, User } from '@/models/fintech'

export const user: User = {
  id: 1,
  username: 'Ricardo Aires',
  email: 'ricardo@finup.com',
  celphone: '(63) 99984-2026',
  tierId: 2,
  points: 1240,
  mainAddressId: 1,
  monthlyIncome: 8750,
  monthlySpending: 5240,
}

export const address: Address = {
  id: 1,
  userId: 1,
  addressString: 'Av. Tocantins, 410 - Centro, Palmas - TO',
  zipCode: '77001-010',
}

export const accounts: BankAccount[] = [
  { id: 1, userId: 1, bank: 'Nubank', type: 'Conta corrente', description: 'Conta principal', agency: '0001', accountNumber: '98241-3' },
  { id: 2, userId: 1, bank: 'XP', type: 'Investimento', description: 'Reserva e renda fixa', agency: '0002', accountNumber: '45081-9' },
]

export const transactions: Transaction[] = [
  { id: 1, userId: 1, amount: 8750, type: 'Receita', description: 'Salario', transactionDate: '2026-05-05', bankAccountId: 1, yield: null },
  { id: 2, userId: 1, amount: 1460, type: 'Despesa', description: 'Aluguel', transactionDate: '2026-05-08', bankAccountId: 1, yield: null },
  { id: 3, userId: 1, amount: 1200, type: 'Investimento', description: 'Tesouro Selic', transactionDate: '2026-05-15', bankAccountId: 2, yield: 108 },
  { id: 4, userId: 1, amount: 184, type: 'Despesa', description: 'Supermercado', transactionDate: '2026-05-23', bankAccountId: 1, yield: null },
]

export const goals: Goal[] = [
  { id: 1, userId: 1, title: 'Reserva de emergencia', amount: 18000, savedAmount: 11340, limitDate: '2026-12-20' },
  { id: 2, userId: 1, title: 'Viagem de ferias', amount: 6500, savedAmount: 2430, limitDate: '2027-01-10' },
]

export const tiers: Tier[] = [
  { id: 1, name: 'Inicio', minPointsRequired: 0, hierarchy: 1 },
  { id: 2, name: 'Explorer', minPointsRequired: 1000, hierarchy: 2 },
  { id: 3, name: 'Expert', minPointsRequired: 2000, hierarchy: 3 },
]

export const rewards: Reward[] = [
  { id: 1, name: 'Cashback turbinado', description: '5% de volta em compras selecionadas', active: true },
  { id: 2, name: 'Consultoria financeira', description: 'Uma sessao com especialista FinUp', active: true },
]

export const challenges: Challenge[] = [
  { id: 1, title: 'Semana sem delivery', minTierId: 1, startDate: '2026-05-20', endDate: '2026-05-30', active: true, rewardId: 1, progress: 70 },
  { id: 2, title: 'Invista por 3 meses', minTierId: 2, startDate: '2026-05-01', endDate: '2026-07-31', active: true, rewardId: 2, progress: 33 },
]

export const completedChallenges: CompletedChallenge[] = [
  { id: 1, userId: 1, challengeId: 3, completedAt: '2026-04-29' },
]
