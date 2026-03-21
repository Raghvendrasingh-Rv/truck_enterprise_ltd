import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import PageShell from "../components/PageShell";
import truckService from "../services/truckService";
import { useAuth } from "../hooks/useAuth";

function formatTruckValue(value) {
  return value.split("_").map((part) => part[0] + part.slice(1).toLowerCase()).join(" ");
}

function statusClasses(status) {
  switch (status) {
    case "AVAILABLE":
      return "bg-emerald-100 text-emerald-800";
    case "IN_TRANSIT":
      return "bg-violet-100 text-violet-800";
    case "MAINTENANCE":
      return "bg-amber-100 text-amber-800";
    case "OFFLINE":
      return "bg-slate-200 text-slate-700";
    default:
      return "bg-mist text-ink";
  }
}

export default function TransporterTrucksPage() {
  const { auth } = useAuth();
  const [trucks, setTrucks] = useState([]);

  useEffect(() => {
    if (!auth?.profileId) return;
    truckService.getTransporterTrucksById(auth.profileId).then(setTrucks).catch(() => setTrucks([]));
  }, [auth?.profileId]);

  return (
    <PageShell title="Truck Management" subtitle="Responsive truck inventory with quick add and edit actions." actions={<Link to="/transporter/trucks/new" className="action-button">Add Truck</Link>}>
      <div className="grid gap-4">
        {trucks.map((truck) => (
          <div key={truck.id} className="panel flex flex-col gap-5 p-5">
            <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
              <div>
                <p className="text-xs uppercase tracking-[0.3em] text-accent">Truck #{truck.id}</p>
                <h3 className="mt-1 text-lg font-bold">{truck.truckNumber}</h3>
                <p className="mt-2 text-sm text-slate-600">
                  {formatTruckValue(truck.truckType)} truck serving from {truck.locationCity}
                </p>
              </div>

              <div className="flex items-center gap-3">
                <span className={`rounded-full px-4 py-2 text-sm font-semibold ${statusClasses(truck.status)}`}>
                  {formatTruckValue(truck.status)}
                </span>
                <Link to={`/transporter/trucks/${truck.id}/edit`} className="secondary-button">Edit</Link>
              </div>
            </div>

            <div className="grid gap-3 md:grid-cols-3">
              <div className="rounded-2xl border border-slate-200 bg-mist p-4">
                <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Truck Type</p>
                <p className="mt-2 text-sm font-semibold text-ink">{formatTruckValue(truck.truckType)}</p>
              </div>

              <div className="rounded-2xl border border-slate-200 bg-mist p-4">
                <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Capacity</p>
                <p className="mt-2 text-sm font-semibold text-ink">{truck.capacityKg} kg</p>
              </div>

              <div className="rounded-2xl border border-slate-200 bg-mist p-4">
                <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Current City</p>
                <p className="mt-2 text-sm font-semibold text-ink">{truck.locationCity}</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </PageShell>
  );
}
