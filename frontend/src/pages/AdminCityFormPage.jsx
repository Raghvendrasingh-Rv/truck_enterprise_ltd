import { useState } from "react";
import { useNavigate } from "react-router-dom";
import PageShell from "../components/PageShell";
import api from "../services/api";

export default function AdminCityFormPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", latitude: "", longitude: "" });

  const onSubmit = async (event) => {
    event.preventDefault();
    await api.post("/cities", { ...form, latitude: Number(form.latitude), longitude: Number(form.longitude) });
    navigate("/admin/cities");
  };

  return (
    <PageShell title="Add City" subtitle="Add a supported city for search, pricing, and booking flows.">
      <form className="panel grid gap-4 p-5 md:grid-cols-3" onSubmit={onSubmit}>
        <input className="field" placeholder="City name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <input className="field" type="number" step="0.0001" placeholder="Latitude" value={form.latitude} onChange={(e) => setForm({ ...form, latitude: e.target.value })} />
        <input className="field" type="number" step="0.0001" placeholder="Longitude" value={form.longitude} onChange={(e) => setForm({ ...form, longitude: e.target.value })} />
        <button className="action-button md:col-span-3" type="submit">Save City</button>
      </form>
    </PageShell>
  );
}
