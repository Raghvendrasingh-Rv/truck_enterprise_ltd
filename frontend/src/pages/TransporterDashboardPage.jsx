import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";
import truckService from "../services/truckService";
import { useAuth } from "../hooks/useAuth";

function formatValue(value) {
  return value.split("_").map((part) => part[0] + part.slice(1).toLowerCase()).join(" ");
}

function StatCard({ label, value, hint }) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white/80 p-5 shadow-sm">
      <p className="text-xs uppercase tracking-[0.28em] text-slate-500">{label}</p>
      <p className="mt-3 text-3xl font-bold text-ink">{value}</p>
      <p className="mt-2 text-sm text-slate-600">{hint}</p>
    </div>
  );
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
    case "CREATED":
      return "bg-sky-100 text-sky-800";
    case "ACCEPTED":
      return "bg-amber-100 text-amber-800";
    case "DELIVERED":
      return "bg-emerald-100 text-emerald-800";
    case "CANCELLED":
      return "bg-rose-100 text-rose-800";
    default:
      return "bg-mist text-ink";
  }
}

export default function TransporterDashboardPage() {
  const { auth } = useAuth();
  const [trucks, setTrucks] = useState([]);
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    if (!auth?.profileId) return;
    truckService.getTransporterTrucksById(auth.profileId).then(setTrucks).catch(() => setTrucks([]));
    bookingService.getTransporterBookings().then(setBookings).catch(() => setBookings([]));
  }, [auth?.profileId]);

  const stats = useMemo(() => ({
    totalTrucks: trucks.length,
    availableTrucks: trucks.filter((truck) => truck.status === "AVAILABLE").length,
    totalBookings: bookings.length,
    newBookings: bookings.filter((booking) => booking.status === "CREATED").length,
    activeDeliveries: bookings.filter((booking) => booking.status === "IN_TRANSIT").length,
  }), [bookings, trucks]);

  const latestBookings = bookings.slice().sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)).slice(0, 4);
  const fleetSnapshot = trucks.slice(0, 4);

  return (
    <PageShell title="Transporter Dashboard" subtitle="Monitor trucks, incoming requests, and live delivery movement from one operational view.">
      <section className="panel overflow-hidden p-0">
        <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
          <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Operations Overview</p>
          <h2 className="mt-2 text-2xl font-bold">{auth?.companyName || auth?.name || "Transporter Workspace"}</h2>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
            Stay on top of incoming booking demand, active deliveries, and fleet readiness without jumping between screens.
          </p>

          <div className="mt-6 flex flex-wrap gap-3">
            <Link className="rounded-full bg-white/15 px-4 py-2 text-sm font-semibold text-white backdrop-blur hover:bg-white/20" to="/transporter/trucks/new">
              Add Truck
            </Link>
            <Link className="rounded-full bg-white/15 px-4 py-2 text-sm font-semibold text-white backdrop-blur hover:bg-white/20" to="/transporter/bookings">
              View Bookings
            </Link>
            <Link className="rounded-full bg-white/15 px-4 py-2 text-sm font-semibold text-white backdrop-blur hover:bg-white/20" to="/transporter/profile">
              Open Profile
            </Link>
          </div>
        </div>
      </section>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-5">
        <StatCard label="Total Trucks" value={stats.totalTrucks} hint="Total vehicles registered under this transporter." />
        <StatCard label="Available Trucks" value={stats.availableTrucks} hint="Ready for new bookings right now." />
        <StatCard label="Total Bookings" value={stats.totalBookings} hint="All bookings received so far." />
        <StatCard label="New Requests" value={stats.newBookings} hint="Bookings waiting for transporter action." />
        <StatCard label="Active Deliveries" value={stats.activeDeliveries} hint="Trips currently moving toward delivery." />
      </div>

      <div className="grid gap-6 xl:grid-cols-[1.05fr_0.95fr]">
        <section className="panel p-6">
          <div className="flex items-center justify-between gap-4">
            <div>
              <p className="text-xs uppercase tracking-[0.28em] text-accent">Latest Bookings</p>
              <h3 className="mt-2 text-xl font-bold text-ink">Recent workflow movement</h3>
            </div>
            <Link className="text-sm font-semibold text-ink" to="/transporter/bookings">See all</Link>
          </div>

          <div className="mt-6 space-y-3">
            {latestBookings.length === 0 ? (
              <div className="rounded-2xl bg-mist p-5 text-sm text-slate-600">No bookings yet. Incoming requests will show up here.</div>
            ) : (
              latestBookings.map((booking) => (
                <Link
                  key={booking.id}
                  to={`/transporter/bookings/${booking.id}`}
                  className="block rounded-2xl border border-slate-200 bg-white/85 p-4 transition hover:border-slate-300 hover:bg-slate-50"
                >
                  <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
                    <div>
                      <p className="text-sm font-semibold text-ink">Booking #{booking.id}</p>
                      <p className="mt-1 text-sm text-slate-600">{booking.source} to {booking.destination}</p>
                      <p className="mt-1 text-xs uppercase tracking-[0.24em] text-slate-400">{booking.truckNumber || "Truck assigned"}</p>
                    </div>
                    <span className={`rounded-full px-4 py-2 text-sm font-semibold ${statusClasses(booking.status)}`}>
                      {formatValue(booking.status)}
                    </span>
                  </div>
                </Link>
              ))
            )}
          </div>
        </section>

        <section className="panel p-6">
          <div className="flex items-center justify-between gap-4">
            <div>
              <p className="text-xs uppercase tracking-[0.28em] text-accent">Fleet Snapshot</p>
              <h3 className="mt-2 text-xl font-bold text-ink">Truck readiness at a glance</h3>
            </div>
            <Link className="text-sm font-semibold text-ink" to="/transporter/trucks">Manage fleet</Link>
          </div>

          <div className="mt-6 space-y-3">
            {fleetSnapshot.length === 0 ? (
              <div className="rounded-2xl bg-mist p-5 text-sm text-slate-600">No trucks added yet. Start by registering your first truck.</div>
            ) : (
              fleetSnapshot.map((truck) => (
                <Link
                  key={truck.id}
                  to={`/transporter/trucks/${truck.id}/edit`}
                  className="block rounded-2xl border border-slate-200 bg-white/85 p-4 transition hover:border-slate-300 hover:bg-slate-50"
                >
                  <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
                    <div>
                      <p className="text-sm font-semibold text-ink">{truck.truckNumber}</p>
                      <p className="mt-1 text-sm text-slate-600">{formatValue(truck.truckType)} • {truck.capacityKg} kg</p>
                      <p className="mt-1 text-xs uppercase tracking-[0.24em] text-slate-400">{truck.locationCity}</p>
                    </div>
                    <span className={`rounded-full px-4 py-2 text-sm font-semibold ${statusClasses(truck.status)}`}>
                      {formatValue(truck.status)}
                    </span>
                  </div>
                </Link>
              ))
            )}
          </div>
        </section>
      </div>
    </PageShell>
  );
}
