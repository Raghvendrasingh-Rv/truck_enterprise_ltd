import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

export default function CustomerBookingsPage() {
  const location = useLocation();
  const createdBooking = location.state?.createdBooking;
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    bookingService
      .getCustomerBookings()
      .then((fetchedBookings) => {
        if (!createdBooking) {
          setBookings(fetchedBookings);
          return;
        }

        const withoutDuplicate = fetchedBookings.filter((booking) => booking.id !== createdBooking.id);
        setBookings([createdBooking, ...withoutDuplicate]);
      })
      .catch(() => setError("Unable to load bookings"));
  }, [createdBooking]);

  return (
    <PageShell title="My Bookings" subtitle="Track booking lifecycle from creation to delivery.">
      {error ? <div className="panel p-4 text-sm text-red-600">{error}</div> : null}
      <div className="grid gap-4">
        {bookings.map((booking) => (
          <Link key={booking.id} to={`/customer/bookings/${booking.id}`} className="panel flex flex-col gap-3 p-5 md:flex-row md:items-center md:justify-between">
            <div>
              <p className="text-xs uppercase tracking-[0.3em] text-accent">Booking #{booking.id}</p>
              <h3 className="mt-1 text-lg font-bold">{booking.source} to {booking.destination}</h3>
              <p className="text-sm text-slate-600">Weight: {booking.weight} kg</p>
            </div>
            <div className="flex items-center gap-4">
              <span className="rounded-full bg-mist px-4 py-2 text-sm font-semibold text-ink">{booking.status}</span>
              <span className="text-sm font-semibold text-ink">Rs {booking.price}</span>
            </div>
          </Link>
        ))}
      </div>
    </PageShell>
  );
}
