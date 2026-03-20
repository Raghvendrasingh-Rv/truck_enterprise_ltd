import { useEffect, useMemo, useState } from "react";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";
import truckService from "../services/truckService";
import { useAuth } from "../hooks/useAuth";

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
    newBookings: bookings.filter((booking) => booking.status === "CREATED").length,
    activeDeliveries: bookings.filter((booking) => booking.status === "IN_TRANSIT").length,
  }), [bookings, trucks]);

  return (
    <PageShell title="Transporter Dashboard" subtitle="Monitor trucks, incoming requests, and live delivery movement.">
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        {[
          ["Total Trucks", stats.totalTrucks],
          ["Available Trucks", stats.availableTrucks],
          ["New Bookings", stats.newBookings],
          ["Active Deliveries", stats.activeDeliveries],
        ].map(([label, value]) => (
          <div key={label} className="panel p-5">
            <p className="text-sm text-slate-500">{label}</p>
            <p className="mt-2 text-3xl font-bold">{value}</p>
          </div>
        ))}
      </div>
    </PageShell>
  );
}
