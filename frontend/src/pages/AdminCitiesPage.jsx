import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import PageShell from "../components/PageShell";
import searchService from "../services/searchService";

export default function AdminCitiesPage() {
  const [cities, setCities] = useState([]);

  useEffect(() => {
    searchService.getCities().then(setCities).catch(() => setCities([]));
  }, []);

  return (
    <PageShell title="Cities" subtitle="Manage supported source and destination cities." actions={<Link className="action-button" to="/admin/cities/new">Add City</Link>}>
      <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-3">
        {cities.map((city) => (
          <div key={city.id} className="panel p-4">
            <p className="text-lg font-semibold">{city.name}</p>
            <p className="text-sm text-slate-500">City ID: {city.id}</p>
          </div>
        ))}
      </div>
    </PageShell>
  );
}
