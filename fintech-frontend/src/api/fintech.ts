import type { AdminEntities, DashboardData, LoginResponse } from '@/models/fintech'

const apiUrl = (import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api').replace(/\/$/, '')
let authenticationToken: string | null = null

interface ApiError {
  message?: string
}

export async function login(email: string, password: string): Promise<LoginResponse> {
  const authentication = await request<LoginResponse>('/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  })
  authenticationToken = authentication.token
  return authentication
}

export async function registerUser(payload: {
  username: string
  email: string
  celphone: string
  password: string
  monthlyIncome: number
  monthlySpending: number
}): Promise<void> {
  await request('/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
}

export async function logout(): Promise<void> {
  const token = authenticationToken
  authenticationToken = null
  if (!token) {
    return
  }
  await request('/auth/logout', {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}` },
  })
}

export async function loadDashboard(userId: number): Promise<DashboardData> {
  return request<DashboardData>(`/users/${userId}/dashboard`)
}

export async function createOwnBankAccount(payload: {
  bank: string
  type: string
  description: string
  agency: string
  accountNumber: string
}): Promise<void> {
  await request('/me/accounts', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authorizationHeaders() },
    body: JSON.stringify(payload),
  })
}

export async function createOwnGoal(payload: {
  title: string
  amount: number
  limitDate: string
}): Promise<void> {
  await request('/me/goals', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authorizationHeaders() },
    body: JSON.stringify(payload),
  })
}

export async function createOwnTransaction(payload: {
  amount: number
  type: 'Receita' | 'Despesa' | 'Investimento'
  description: string
  transactionDate: string
  bankAccountId: number
  yield: number | null
  goalId: number | null
}): Promise<void> {
  await request('/me/transactions', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authorizationHeaders() },
    body: JSON.stringify(payload),
  })
}

export async function createEntity(path: string, payload: Record<string, unknown>): Promise<void> {
  await request(`/${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authorizationHeaders() },
    body: JSON.stringify(payload),
  })
}

export async function updateEntity(path: string, id: number, payload: Record<string, unknown>): Promise<void> {
  await request(`/${path}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', ...authorizationHeaders() },
    body: JSON.stringify(payload),
  })
}

export async function deleteEntity(path: string, id: number): Promise<void> {
  await request(`/${path}/${id}`, {
    method: 'DELETE',
    headers: { ...authorizationHeaders() },
  })
}

export async function loadAdminEntities(): Promise<AdminEntities> {
  return request<AdminEntities>('/admin/entities', { headers: authorizationHeaders() })
}

export async function createAdminEntity(endpoint: string, payload: Record<string, unknown>): Promise<void> {
  await request(`/admin/${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authorizationHeaders() },
    body: JSON.stringify(payload),
  })
}

function authorizationHeaders(): Record<string, string> {
  return authenticationToken ? { Authorization: `Bearer ${authenticationToken}` } : {}
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response

  try {
    response = await fetch(`${apiUrl}${path}`, init)
  } catch {
    throw new Error('Não foi possível conectar à API. Confirme se o backend está em execução.')
  }

  if (!response.ok) {
    let error: ApiError | undefined
    try {
      error = (await response.json()) as ApiError
    } catch {
      error = undefined
    }
    throw new Error(error?.message ?? 'Ocorreu um erro ao consultar a API.')
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json() as Promise<T>
}
