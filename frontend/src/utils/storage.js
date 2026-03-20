const AUTH_KEY = "truck_platform_auth";

export function getStoredAuth() {
  const raw = localStorage.getItem(AUTH_KEY);
  return raw ? JSON.parse(raw) : null;
}

export function setStoredAuth(value) {
  localStorage.setItem(AUTH_KEY, JSON.stringify(value));
}

export function clearStoredAuth() {
  localStorage.removeItem(AUTH_KEY);
}
