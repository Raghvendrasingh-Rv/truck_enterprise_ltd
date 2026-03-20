import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function RegisterPage() {
  const navigate = useNavigate();
  const { registerCustomer } = useAuth();
  const [form, setForm] = useState({ name: "", email: "", phone: "", password: "" });
  const [error, setError] = useState("");

  const onSubmit = async (event) => {
    event.preventDefault();
    try {
      setError("");
      await registerCustomer(form);
      navigate("/login");
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <form className="space-y-5" onSubmit={onSubmit}>
      <div>
        <p className="text-xs uppercase tracking-[0.35em] text-accent">Customer Onboarding</p>
        <h2 className="mt-2 text-3xl font-bold">Create your booking account</h2>
      </div>
      {["name", "email", "phone", "password"].map((field) => (
        <input
          key={field}
          className="field"
          type={field === "password" ? "password" : "text"}
          placeholder={field[0].toUpperCase() + field.slice(1)}
          value={form[field]}
          onChange={(e) => setForm({ ...form, [field]: e.target.value })}
        />
      ))}
      {error ? <p className="text-sm text-red-600">{error}</p> : null}
      <button className="action-button w-full" type="submit">Register</button>
      <p className="text-sm text-slate-600">Already registered? <Link className="font-semibold text-ink" to="/login">Login</Link></p>
    </form>
  );
}
