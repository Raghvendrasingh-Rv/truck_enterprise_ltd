import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import PageShell from "../components/PageShell";
import searchService from "../services/searchService";

export default function CustomerSearchPage() {
  const navigate = useNavigate();
  const [cities, setCities] = useState([]);
  const [form, setForm] = useState({ sourceCity: "", destinationCity: "", weight: "" });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    searchService.getCities().then(setCities).catch(() => setCities([]));
  }, []);

  const onSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      const payload = { ...form, weight: Number(form.weight) };
      const results = await searchService.searchTrucks(payload);
      navigate("/customer/results", { state: { query: payload, results } });
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageShell title="Search Trucks" subtitle="Pick a route, compare available trucks, and move toward booking.">
      <form className="panel grid gap-4 p-4 md:grid-cols-4 md:items-end" onSubmit={onSubmit}>
        <select className="field" value={form.sourceCity} onChange={(e) => setForm({ ...form, sourceCity: e.target.value })}>
          <option value="">Source city</option>
          {cities.map((city) => <option key={city.id} value={city.name}>{city.name}</option>)}
        </select>
        <select className="field" value={form.destinationCity} onChange={(e) => setForm({ ...form, destinationCity: e.target.value })}>
          <option value="">Destination city</option>
          {cities.map((city) => <option key={city.id} value={city.name}>{city.name}</option>)}
        </select>
        <input className="field" type="number" placeholder="Weight (kg)" value={form.weight} onChange={(e) => setForm({ ...form, weight: e.target.value })} />
        <button className="action-button w-full" type="submit">{loading ? "Searching..." : "Search Trucks"}</button>
      </form>
    </PageShell>
  );
}
