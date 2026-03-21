import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import PageShell from "../components/PageShell";
import searchService from "../services/searchService";
import truckService from "../services/truckService";

const baseForm = {
  truckNumber: "",
  truckType: "BOX_TRUCK",
  capacityKg: "",
  locationCity: "",
  status: "AVAILABLE",
};

const truckTypes = ["FLATBED", "REFRIGERATED", "TANKER", "BOX_TRUCK", "PICKUP", "DUMP_TRUCK", "AUTO_CARRIER"];
const truckStatuses = ["AVAILABLE", "IN_TRANSIT", "MAINTENANCE", "OFFLINE"];

function formatTruckType(value) {
  return value.split("_").map((part) => part[0] + part.slice(1).toLowerCase()).join(" ");
}

export default function TruckFormPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState(baseForm);
  const [cities, setCities] = useState([]);
  const [loading, setLoading] = useState(Boolean(id));
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    searchService.getCities().then(setCities).catch(() => setCities([]));
  }, []);

  useEffect(() => {
    if (!id) {
      setForm(baseForm);
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    truckService.getTruckById(id)
      .then((truck) => {
        setForm({
          truckNumber: truck.truckNumber ?? "",
          truckType: truck.truckType ?? "BOX_TRUCK",
          capacityKg: truck.capacityKg ?? "",
          locationCity: truck.locationCity ?? "",
          status: truck.status ?? "AVAILABLE",
        });
      })
      .catch(() => setError("We could not load this truck right now."))
      .finally(() => setLoading(false));
  }, [id]);

  const pageTitle = id ? "Edit Truck" : "Add Truck";
  const pageSubtitle = id
    ? "Update truck details, availability, and location so booking and search stay accurate."
    : "Add a new truck with the details customers and booking workflows will rely on.";

  const onSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError("");

    try {
      const payload = {
        truckNumber: form.truckNumber,
        truckType: form.truckType,
        capacityKg: Number(form.capacityKg),
        locationCity: form.locationCity,
        ...(id ? { status: form.status } : {}),
      };

      if (id) {
        await truckService.updateTruck(id, payload);
      } else {
        await truckService.addTruck(payload);
      }

      navigate("/transporter/trucks");
    } catch {
      setError(id ? "We could not update this truck right now." : "We could not add this truck right now.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <PageShell title={pageTitle} subtitle={pageSubtitle}>
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}

      <form className="panel p-6" onSubmit={onSubmit}>
          <div>
            <p className="text-xs uppercase tracking-[0.28em] text-accent">{id ? "Edit Details" : "Truck Details"}</p>
            <h3 className="mt-2 text-xl font-bold text-ink">{id ? "Update truck information" : "Add a truck"}</h3>
            <p className="mt-2 text-sm leading-6 text-slate-600">
              Fill in the truck profile that customers and booking workflows will use during search and fulfillment.
            </p>
          </div>

          {loading ? (
            <div className="mt-6 rounded-2xl bg-mist p-5 text-sm text-slate-600">Loading truck details...</div>
          ) : (
            <div className="mt-6 grid gap-4">
              <div>
                <label className="mb-2 block text-sm font-semibold text-ink">Truck Number</label>
                <input
                  className="field"
                  placeholder="MH12AB1234"
                  value={form.truckNumber}
                  onChange={(event) => setForm((current) => ({ ...current, truckNumber: event.target.value.toUpperCase() }))}
                />
              </div>

              <div>
                <label className="mb-2 block text-sm font-semibold text-ink">Truck Type</label>
                <select
                  className="field"
                  value={form.truckType}
                  onChange={(event) => setForm((current) => ({ ...current, truckType: event.target.value }))}
                >
                  {truckTypes.map((truckType) => (
                    <option key={truckType} value={truckType}>
                      {formatTruckType(truckType)}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="mb-2 block text-sm font-semibold text-ink">Capacity (kg)</label>
                <input
                  className="field"
                  min="0"
                  type="number"
                  placeholder="5000"
                  value={form.capacityKg}
                  onChange={(event) => setForm((current) => ({ ...current, capacityKg: event.target.value }))}
                />
              </div>

              <div>
                <label className="mb-2 block text-sm font-semibold text-ink">Current City</label>
                <select
                  className="field"
                  value={form.locationCity}
                  onChange={(event) => setForm((current) => ({ ...current, locationCity: event.target.value }))}
                >
                  <option value="">Select city</option>
                  {cities.map((city) => (
                    <option key={city.id} value={city.name}>
                      {city.name}
                    </option>
                  ))}
                </select>
              </div>

              {id ? (
                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Truck Status</label>
                  <select
                    className="field"
                    value={form.status}
                    onChange={(event) => setForm((current) => ({ ...current, status: event.target.value }))}
                  >
                    {truckStatuses.map((status) => (
                      <option key={status} value={status}>
                        {formatTruckType(status)}
                      </option>
                    ))}
                  </select>
                </div>
              ) : null}

              <div className="flex flex-col gap-3 pt-2 sm:flex-row">
                <button className="action-button flex-1" disabled={saving || loading} type="submit">
                  {saving ? (id ? "Updating..." : "Adding...") : id ? "Save Truck Changes" : "Add Truck"}
                </button>
                <button className="secondary-button flex-1" onClick={() => navigate("/transporter/trucks")} type="button">
                  Cancel
                </button>
              </div>
              </div>
            )}
      </form>
    </PageShell>
  );
}
