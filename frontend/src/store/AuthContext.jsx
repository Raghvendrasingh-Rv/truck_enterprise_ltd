import { createContext, useEffect, useMemo, useState } from "react";
import authService from "../services/authService";

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => authService.getCurrentUser());

  useEffect(() => {
    setAuth(authService.getCurrentUser());
  }, []);

  const value = useMemo(
    () => ({
      auth,
      isAuthenticated: Boolean(auth?.token),
      role: auth?.role || null,
      loginCustomer: async (payload) => {
        const next = await authService.loginCustomer(payload);
        setAuth(next);
        return next;
      },
      registerCustomer: authService.registerCustomer,
      loginTransporter: async (payload) => {
        const next = await authService.loginTransporter(payload);
        setAuth(next);
        return next;
      },
      registerTransporter: authService.registerTransporter,
      syncAuth: (patch) => {
        const next = authService.syncAuth(patch);
        setAuth(next);
        return next;
      },
      logout: () => {
        authService.logout();
        setAuth(null);
      },
    }),
    [auth]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
