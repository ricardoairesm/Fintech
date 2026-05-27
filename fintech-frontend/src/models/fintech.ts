export interface User {
  id: number
  username: string
  email: string
  celphone: string
  userType: 'USER' | 'ADMIN'
  tierId: number
  points: number
  mainAddressId: number | null
  monthlyIncome: number
  monthlySpending: number
}

export interface Address {
  id: number
  userId: number
  addressString: string
  zipCode: string
}

export interface BankAccount {
  id: number
  userId: number
  bank: string
  type: string
  description: string
  agency: string
  accountNumber: string
}

export interface Transaction {
  id: number
  userId: number
  amount: number
  type: 'Receita' | 'Despesa' | 'Investimento'
  description: string
  transactionDate: string
  bankAccountId: number
  yield: number | null
  goalId: number | null
}

export interface Goal {
  id: number
  userId: number
  amount: number
  savedAmount: number
  limitDate: string
  title: string
}

export interface Tier {
  id: number
  name: string
  minPointsRequired: number
  hierarchy: number
}

export interface Reward {
  id: number
  name: string
  description: string
  active: boolean
}

export interface Challenge {
  id: number
  minTierId: number
  startDate: string
  endDate: string
  active: boolean
  rewardId: number
  title: string
  progress: number
}

export interface CompletedChallenge {
  id: number
  userId: number
  challengeId: number
  completedAt: string
}

export interface DashboardSummary {
  balance: number
  totalIncome: number
  totalExpense: number
  totalInvested: number
}

export interface DashboardData {
  user: User
  address: Address | null
  accounts: BankAccount[]
  transactions: Transaction[]
  goals: Goal[]
  tiers: Tier[]
  rewards: Reward[]
  challenges: Challenge[]
  completedChallenges: CompletedChallenge[]
  summary: DashboardSummary
}

export interface LoginResponse {
  user: User
  token: string
}

export interface AdminEntities {
  users: User[]
  addresses: Address[]
  accounts: BankAccount[]
  transactions: Transaction[]
  goals: Goal[]
  tiers: Tier[]
  rewards: Reward[]
  challenges: Challenge[]
  completedChallenges: CompletedChallenge[]
}
