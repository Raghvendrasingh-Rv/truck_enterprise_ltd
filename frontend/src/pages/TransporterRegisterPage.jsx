import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

const initialForm = {
  companyName: "",
  name: "",
  email: "",
  mobileNumber: "",
  yearsOfExperience: "",
  address: "",
  password: "",
};

export default function TransporterRegisterPage() {
  const navigate = useNavigate();
  const { registerTransporter } = useAuth();
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState("");

  const onSubmit = async (event) => {
    event.preventDefault();
    try {
      setError("");
      await registerTransporter({ ...form, yearsOfExperience: Number(form.yearsOfExperience) });
      navigate("/transporter/login");
    } catch (err) {
      setError(err.response?.data?.message || "Transporter registration failed");
    }
  };

  return (
    <form className="space-y-4" onSubmit={onSubmit}>
      <div>
        <p className="text-xs uppercase tracking-[0.35em] text-accent">Transporter Onboarding</p>
        <h2 className="mt-2 text-3xl font-bold">Create transporter account</h2>
      </div>
      {Object.entries(initialForm).map(([field]) => (
        <input
          key={field}
          className="field"
          type={field === "password" ? "password" : field === "yearsOfExperience" ? "number" : "text"}
          placeholder={field}
          value={form[field]}
          onChange={(e) => setForm({ ...form, [field]: e.target.value })}
        />
      ))}
      {error ? <p className="text-sm text-red-600">{error}</p> : null}
      <button className="action-button w-full" type="submit">Register Transporter</button>
      <p className="text-sm text-slate-600">Already onboarded? <Link className="font-semibold text-ink" to="/transporter/login">Login</Link></p>
    </form>
  );
}
