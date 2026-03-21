import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

function statusClasses(status) {
  switch (status) {
    case "CREATED":
      return "bg-sky-100 text-sky-800";
    case "ACCEPTED":
      return "bg-amber-100 text-amber-800";
    case "IN_TRANSIT":
      return "bg-violet-100 text-violet-800";
    case "DELIVERED":
      return "bg-emerald-100 text-emerald-800";
    case "CANCELLED":
      return "bg-rose-100 text-rose-800";
    default:
      return "bg-mist text-ink";
  }
}

export default function CustomerBookingsPage() {
  const location = useLocation();
  const createdBooking = location.state?.createdBooking;
  const updatedBooking = location.state?.updatedBooking;
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    bookingService
      .getCustomerBookings()
      .then((fetchedBookings) => {
        const priorityBooking = updatedBooking ?? createdBooking;

        if (!priorityBooking) {
          setBookings(fetchedBookings);
          return;
        }

        const withoutDuplicate = fetchedBookings.filter((booking) => booking.id !== priorityBooking.id);
        setBookings([priorityBooking, ...withoutDuplicate]);
      })
      .catch(() => setError("Unable to load bookings"));
  }, [createdBooking, updatedBooking]);

  return (
    <PageShell title="My Bookings" subtitle="Track each shipment from request to delivery in one customer-friendly timeline.">
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}

      <section className="panel hidden overflow-hidden p-0 md:block">
        <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
          <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Customer Bookings</p>
          <h2 className="mt-2 text-2xl font-bold">Your shipment history and active trips</h2>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
            Review every booking, watch the current status, and open any shipment to see truck and transporter details.
          </p>
        </div>
      </section>

      <div className="grid gap-4">
        {bookings.map((booking) => (
          <Link
            key={booking.id}
            to={`/customer/bookings/${booking.id}`}
            className="panel overflow-hidden p-0 transition hover:-translate-y-0.5 hover:shadow-lg"
          >
            <div className="border-b border-slate-200 bg-slate-50 p-5">
              <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
                <div>
                  <p className="text-xs uppercase tracking-[0.3em] text-accent">Booking #{booking.id}</p>
                  <h3 className="mt-2 text-xl font-bold text-ink">{booking.source} to {booking.destination}</h3>
                  <p className="mt-2 text-sm text-slate-600">
                    Shipment weight: {booking.weight} kg
                  </p>
                </div>

                <div className="flex flex-wrap items-center gap-3">
                  <span className={`rounded-full px-4 py-2 text-sm font-semibold ${statusClasses(booking.status)}`}>
                    {booking.status.split("_").map((part) => part[0] + part.slice(1).toLowerCase()).join(" ")}
                  </span>
                  <div className="rounded-2xl bg-amber-50 px-4 py-3 text-right">
                    <p className="text-xs uppercase tracking-[0.2em] text-amber-700">Booking Price</p>
                    <p className="mt-1 text-lg font-bold text-amber-900">Rs {booking.price}</p>
                  </div>
                </div>
              </div>
            </div>
            <div className="flex items-center justify-between gap-4 p-5 text-sm text-slate-600">
              <span>{booking.transporterName || "Transporter assigned"}</span>
              <span className="font-semibold text-ink">Open details</span>
            </div>
          </Link>
        ))}
      </div>

      {bookings.length === 0 && !error ? (
        <div className="panel p-6 text-sm text-slate-600">
          No bookings yet. Search for a truck and create your first shipment request.
        </div>
      ) : null}
    </PageShell>
  );
}
