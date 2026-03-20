import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function LoginPage() {
  const navigate = useNavigate();
  const { loginCustomer } = useAuth();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");

  const onSubmit = async (event) => {
    event.preventDefault();
    try {
      setError("");
      const auth = await loginCustomer(form);
      navigate(auth.role === "ADMIN" ? "/admin/cities" : "/customer/search");
    } catch (err) {
      setError(err.response?.data?.message || "Login failed");
    }
  };

  return (
    <form className="space-y-5" onSubmit={onSubmit}>
      <div>
        <p className="text-xs uppercase tracking-[0.35em] text-accent">Customer Access</p>
        <h2 className="mt-2 text-3xl font-bold">Login to your account</h2>
      </div>
      <input className="field" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
      <input className="field" type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
      {error ? <p className="text-sm text-red-600">{error}</p> : null}
      <button className="action-button w-full" type="submit">Login</button>
      <p className="text-sm text-slate-600">Need an account? <Link className="font-semibold text-ink" to="/register">Register</Link></p>
    </form>
  );
}
