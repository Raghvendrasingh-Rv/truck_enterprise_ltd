import api from "./api";
import { clearStoredAuth, getStoredAuth, setStoredAuth } from "../utils/storage";

const authService = {
  async loginCustomer(payload) {
    const { data } = await api.post("/auth/login", payload);
    const authState = {
      token: data.data.token,
      role: data.data.role,
      profileId: data.data.userId,
      name: data.data.name,
      email: data.data.email,
      mode: "CUSTOMER",
    };
    setStoredAuth(authState);
    return authState;
  },

  async registerCustomer(payload) {
    const { data } = await api.post("/auth/register", payload);
    return data.data;
  },

  async loginTransporter(payload) {
    const { data } = await api.post("/transporter-auth/login", payload);
    const authState = {
      token: data.data.token,
      role: "TRANSPORTER",
      profileId: data.data.transporterId,
      name: data.data.name,
      email: data.data.email,
      companyName: data.data.companyName,
      mode: "TRANSPORTER",
    };
    setStoredAuth(authState);
    return authState;
  },

  async registerTransporter(payload) {
    const { data } = await api.post("/transporter-auth/register", payload);
    return data.data;
  },

  logout() {
    clearStoredAuth();
  },

  syncAuth(patch) {
    const current = getStoredAuth();
    if (!current) return null;

    const next = { ...current, ...patch };
    setStoredAuth(next);
    return next;
  },

  getCurrentUser() {
    return getStoredAuth();
  },
};

export default authService;
