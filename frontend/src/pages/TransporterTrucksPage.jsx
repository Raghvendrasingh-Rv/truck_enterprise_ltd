import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import PageShell from "../components/PageShell";
import truckService from "../services/truckService";
import { useAuth } from "../hooks/useAuth";

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
          <div key={truck.id} className="panel flex flex-col gap-4 p-5 md:flex-row md:items-center md:justify-between">
            <div>
              <h3 className="text-lg font-bold">{truck.truckNumber}</h3>
              <p className="text-sm text-slate-600">{truck.truckType} • {truck.capacityKg} kg • {truck.locationCity}</p>
            </div>
            <div className="flex gap-3">
              <span className="rounded-full bg-mist px-4 py-2 text-sm font-semibold">{truck.status}</span>
              <Link to={`/transporter/trucks/${truck.id}/edit`} className="secondary-button">Edit</Link>
            </div>
          </div>
        ))}
      </div>
    </PageShell>
  );
}
