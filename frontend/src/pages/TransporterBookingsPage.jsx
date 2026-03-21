import { useEffect, useMemo, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

function statusTone(status) {
  switch (status) {
    case "CREATED":
      return {
        panel: "border-sky-200 bg-sky-50/70",
        badge: "bg-sky-100 text-sky-800",
      };
    case "ACCEPTED":
      return {
        panel: "border-amber-200 bg-amber-50/70",
        badge: "bg-amber-100 text-amber-800",
      };
    case "IN_TRANSIT":
      return {
        panel: "border-violet-200 bg-violet-50/70",
        badge: "bg-violet-100 text-violet-800",
      };
    case "DELIVERED":
      return {
        panel: "border-emerald-200 bg-emerald-50/70",
        badge: "bg-emerald-100 text-emerald-800",
      };
    case "CANCELLED":
      return {
        panel: "border-rose-200 bg-rose-50/70",
        badge: "bg-rose-100 text-rose-800",
      };
    default:
      return {
        panel: "border-slate-200 bg-white",
        badge: "bg-slate-100 text-slate-700",
      };
  }
}

function formatValue(value) {
  if (!value) return "-";
  return value.split("_").map((part) => part[0] + part.slice(1).toLowerCase()).join(" ");
}

function BookingCard({ booking, badgeClass }) {
  return (
    <Link
      to={`/transporter/bookings/${booking.id}`}
      className="block rounded-2xl border border-slate-200 bg-white/90 p-4 shadow-sm transition hover:-translate-y-0.5 hover:border-slate-300 hover:bg-white"
    >
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm font-semibold text-ink">Booking #{booking.id}</p>
          <p className="mt-1 text-sm text-slate-600">{booking.source} to {booking.destination}</p>
        </div>
        <span className={`rounded-full px-3 py-1 text-xs font-semibold ${badgeClass}`}>
          {formatValue(booking.status)}
        </span>
      </div>

      <div className="mt-4 grid gap-3 sm:grid-cols-2 xl:grid-cols-4">
        <div className="rounded-xl bg-mist p-3">
          <p className="text-[11px] uppercase tracking-[0.22em] text-slate-500">Truck</p>
          <p className="mt-2 text-sm font-semibold text-ink">{booking.truckNumber || "-"}</p>
          <p className="mt-1 text-xs text-slate-500">{formatValue(booking.truckType)}</p>
        </div>

        <div className="rounded-xl bg-mist p-3">
          <p className="text-[11px] uppercase tracking-[0.22em] text-slate-500">Commercials</p>
          <p className="mt-2 text-sm font-semibold text-ink">Rs {booking.price ?? "-"}</p>
          <p className="mt-1 text-xs text-slate-500">{booking.weight ?? "-"} kg load</p>
        </div>

        <div className="rounded-xl bg-mist p-3">
          <p className="text-[11px] uppercase tracking-[0.22em] text-slate-500">Customer</p>
          <p className="mt-2 text-sm font-semibold text-ink">{booking.customerName || "Customer"}</p>
          <p className="mt-1 text-xs text-slate-500">{booking.customerEmail || "Open details for contact"}</p>
        </div>

        <div className="rounded-xl bg-mist p-3">
          <p className="text-[11px] uppercase tracking-[0.22em] text-slate-500">Action</p>
          <p className="mt-2 text-sm font-semibold text-ink">Open details</p>
          <p className="mt-1 text-xs text-slate-500">Review and update workflow</p>
        </div>
      </div>
    </Link>
  );
}

export default function TransporterBookingsPage() {
  const location = useLocation();
  const [bookings, setBookings] = useState([]);
  const [activeTab, setActiveTab] = useState("CREATED");

  useEffect(() => {
    bookingService.getTransporterBookings().then((fetched) => {
      const updatedBooking = location.state?.updatedBooking;
      if (!updatedBooking) {
        setBookings(fetched);
        return;
      }
      const withoutDuplicate = fetched.filter((booking) => booking.id !== updatedBooking.id);
      setBookings([updatedBooking, ...withoutDuplicate]);
    }).catch(() => setBookings([]));
  }, [location.state]);

  const tabs = useMemo(() => ([
    { key: "CREATED", label: "New Requests", emptyCopy: "New customer requests will appear here." },
    { key: "ACCEPTED", label: "Accepted", emptyCopy: "Accepted jobs waiting to start will appear here." },
    { key: "IN_TRANSIT", label: "In Transit", emptyCopy: "Active deliveries will appear here." },
    { key: "DELIVERED", label: "Completed", emptyCopy: "Delivered bookings will appear here." },
    { key: "CANCELLED", label: "Cancelled", emptyCopy: "Cancelled bookings will appear here." },
  ]), []);

  const grouped = useMemo(() => ({
    CREATED: bookings.filter((booking) => booking.status === "CREATED"),
    ACCEPTED: bookings.filter((booking) => booking.status === "ACCEPTED"),
    IN_TRANSIT: bookings.filter((booking) => booking.status === "IN_TRANSIT"),
    DELIVERED: bookings.filter((booking) => booking.status === "DELIVERED"),
    CANCELLED: bookings.filter((booking) => booking.status === "CANCELLED"),
  }), [bookings]);

  const currentTab = tabs.find((tab) => tab.key === activeTab) ?? tabs[0];
  const currentItems = grouped[currentTab.key] ?? [];
  const tone = statusTone(currentTab.key);

  return (
    <PageShell title="Transporter Bookings" subtitle="Switch between workflow states and focus on one queue at a time.">
      <section className="panel overflow-hidden p-0">
        <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
          <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Booking Workflow</p>
          <h2 className="mt-2 text-2xl font-bold">Requests to delivery, one queue at a time</h2>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
            Use the status tabs to focus on the next action queue instead of scanning a wide board with every booking at once.
          </p>
        </div>
      </section>

      <section className="panel p-4 md:p-6">
        <div className="overflow-x-auto pb-1 [-ms-overflow-style:none] [scrollbar-width:none] [&::-webkit-scrollbar]:hidden">
          <div className="flex min-w-max gap-2">
            {tabs.map((tab) => {
              const isActive = tab.key === activeTab;
              const count = grouped[tab.key]?.length ?? 0;

              return (
                <button
                  key={tab.key}
                  className={[
                    "rounded-full px-4 py-2.5 text-sm font-semibold transition",
                    isActive
                      ? "bg-[linear-gradient(135deg,#0f172a,#1e293b)] text-white shadow-lg shadow-slate-900/20"
                      : "border border-slate-200 bg-white text-slate-700 hover:border-slate-300 hover:bg-slate-50",
                  ].join(" ")}
                  onClick={() => setActiveTab(tab.key)}
                  type="button"
                >
                  {tab.label} ({count})
                </button>
              );
            })}
          </div>
        </div>

        <div className={`mt-6 rounded-3xl border p-4 md:p-5 ${tone.panel}`}>
          <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
            <div>
              <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Current Queue</p>
              <h3 className="mt-2 text-xl font-bold text-ink">{currentTab.label}</h3>
            </div>
            <span className={`w-fit rounded-full px-4 py-2 text-sm font-semibold ${tone.badge}`}>
              {currentItems.length} bookings
            </span>
          </div>

          <div className="mt-5 space-y-3">
            {currentItems.length === 0 ? (
              <div className="rounded-2xl border border-dashed border-slate-300 bg-white/80 p-5 text-sm text-slate-500">
                {currentTab.emptyCopy}
              </div>
            ) : (
              currentItems.map((booking) => (
                <BookingCard key={booking.id} booking={booking} badgeClass={tone.badge} />
              ))
            )}
          </div>
        </div>
      </section>
    </PageShell>
  );
}
