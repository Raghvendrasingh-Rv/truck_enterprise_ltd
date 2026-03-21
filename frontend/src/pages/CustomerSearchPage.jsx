import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import PageShell from "../components/PageShell";
import searchService from "../services/searchService";

function SearchStat({ label, value, tone = "light" }) {
  const palette = tone === "dark"
    ? "border-white/15 bg-white/10 text-white"
    : "border-slate-200 bg-white/80 text-ink";

  return (
    <div className={`rounded-2xl border p-4 shadow-sm ${palette}`}>
      <p className={`text-xs uppercase tracking-[0.25em] ${tone === "dark" ? "text-slate-300" : "text-slate-500"}`}>{label}</p>
      <p className="mt-2 text-sm font-semibold">{value}</p>
    </div>
  );
}

export default function CustomerSearchPage() {
  const navigate = useNavigate();
  const [cities, setCities] = useState([]);
  const [form, setForm] = useState({ sourceCity: "", destinationCity: "", weight: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    searchService.getCities().then(setCities).catch(() => setCities([]));
  }, []);

  const onSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = { ...form, weight: Number(form.weight) };
      const results = await searchService.searchTrucks(payload);
      navigate("/customer/results", { state: { query: payload, results } });
    } catch {
      setError("We could not search trucks right now. Please check the route details and try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageShell title="Search Trucks" subtitle="Choose your route, compare available transporters, and move straight into booking.">
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}

      <section className="panel hidden overflow-hidden p-0 md:block">
        <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
          <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Customer Search</p>
          <h2 className="mt-2 text-2xl font-bold">Find the right truck for your route</h2>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
            Search by source, destination, and weight to compare available trucks with price, capacity, and transporter details.
          </p>

          <div className="mt-6 grid gap-4 md:grid-cols-3">
            <SearchStat label="Supported Cities" value={`${cities.length} active options`} tone="dark" />
            <SearchStat label="Booking Flow" value="Search, compare, and book in one pass" tone="dark" />
            <SearchStat label="Search Basis" value="City, truck capacity, and availability" tone="dark" />
          </div>
        </div>
      </section>

      <form className="panel p-6" onSubmit={onSubmit}>
          <div>
            <p className="text-xs uppercase tracking-[0.28em] text-accent">Search Form</p>
            <h3 className="mt-2 text-xl font-bold text-ink">Route and shipment details</h3>
            <p className="mt-2 text-sm leading-6 text-slate-600">
              Enter the route and shipment load. We will return trucks that can carry the weight and are currently available.
            </p>
          </div>

          <div className="mt-6 grid gap-4 md:grid-cols-2">
            <div>
              <label className="mb-2 block text-sm font-semibold text-ink">Source City</label>
              <select
                className="field"
                value={form.sourceCity}
                onChange={(event) => setForm((current) => ({ ...current, sourceCity: event.target.value }))}
              >
                <option value="">Select source city</option>
                {cities.map((city) => (
                  <option key={city.id} value={city.name}>{city.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="mb-2 block text-sm font-semibold text-ink">Destination City</label>
              <select
                className="field"
                value={form.destinationCity}
                onChange={(event) => setForm((current) => ({ ...current, destinationCity: event.target.value }))}
              >
                <option value="">Select destination city</option>
                {cities.map((city) => (
                  <option key={city.id} value={city.name}>{city.name}</option>
                ))}
              </select>
            </div>

            <div className="md:col-span-2">
              <label className="mb-2 block text-sm font-semibold text-ink">Shipment Weight (kg)</label>
              <input
                className="field"
                min="0"
                type="number"
                placeholder="Enter shipment weight"
                value={form.weight}
                onChange={(event) => setForm((current) => ({ ...current, weight: event.target.value }))}
              />
            </div>
          </div>

          <button className="action-button mt-6 w-full" disabled={loading} type="submit">
            {loading ? "Searching Trucks..." : "Search Available Trucks"}
          </button>
      </form>
    </PageShell>
  );
}
