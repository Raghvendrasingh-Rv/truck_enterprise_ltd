import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

export default function TransporterBookingsPage() {
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    bookingService.getTransporterBookings().then(setBookings).catch(() => setBookings([]));
  }, []);

  const groups = useMemo(() => ({
    created: bookings.filter((booking) => booking.status === "CREATED"),
    accepted: bookings.filter((booking) => booking.status === "ACCEPTED"),
    inTransit: bookings.filter((booking) => booking.status === "IN_TRANSIT"),
    completed: bookings.filter((booking) => booking.status === "DELIVERED"),
  }), [bookings]);

  return (
    <PageShell title="Transporter Bookings" subtitle="Group requests by workflow stage and act quickly.">
      <div className="grid gap-4 xl:grid-cols-4">
        {[
          ["New Requests", groups.created],
          ["Accepted", groups.accepted],
          ["In Transit", groups.inTransit],
          ["Completed", groups.completed],
        ].map(([label, items]) => (
          <section key={label} className="panel p-4">
            <h3 className="text-lg font-bold">{label}</h3>
            <div className="mt-4 space-y-3">
              {items.map((booking) => (
                <Link key={booking.id} to={`/transporter/bookings/${booking.id}`} className="block rounded-xl border border-slate-200 p-3 hover:bg-mist">
                  <p className="text-sm font-semibold">Booking #{booking.id}</p>
                  <p className="text-xs text-slate-500">{booking.source} to {booking.destination}</p>
                </Link>
              ))}
            </div>
          </section>
        ))}
      </div>
    </PageShell>
  );
}
