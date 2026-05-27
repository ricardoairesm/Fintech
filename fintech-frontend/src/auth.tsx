import { createContext, useContext, useState, type ReactNode } from 'react'

import { login as apiLogin, logout as apiLogout } from '@/api/fintech'
import type { User } from '@/models/fintech'

interface AuthContextValue {
  user: User | null
  signIn: (email: string, password: string) => Promise<User>
  signOut: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)

  async function signIn(email: string, password: string) {
    const authentication = await apiLogin(email, password)
    setUser(authentication.user)
    return authentication.user
  }

  async function signOut() {
    await apiLogout()
    setUser(null)
  }

  return <AuthContext.Provider value={{ user, signIn, signOut }}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de AuthProvider.')
  }
  return context
}
