import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

function formatTruckType(value) {
  if (!value) return "-";
  return value.split("_").map((part) => part[0] + part.slice(1).toLowerCase()).join(" ");
}

function StatChip({ label, value }) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white/85 p-4 shadow-sm">
      <p className="text-xs uppercase tracking-[0.24em] text-slate-500">{label}</p>
      <p className="mt-2 text-sm font-semibold text-ink">{value}</p>
    </div>
  );
}

export default function SearchResultsPage() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const results = state?.results || [];
  const query = state?.query;
  const [bookingTruckId, setBookingTruckId] = useState(null);
  const [expandedTruckId, setExpandedTruckId] = useState(null);

  const handleBookNow = async (truck) => {
    if (!query) return;

    setBookingTruckId(truck.truckId);
    try {
      const createdBooking = await bookingService.createBooking({
        truckId: truck.truckId,
        source: query.sourceCity,
        destination: query.destinationCity,
        weight: query.weight,
      });

      navigate("/customer/bookings", { state: { createdBooking } });
    } finally {
      setBookingTruckId(null);
    }
  };

  return (
    <PageShell title="Search Results" subtitle="Compare truck type, transporter strength, and estimated price before you book.">
      {!query ? (
        <div className="panel p-6 text-sm text-slate-600">Run a search first to view matching trucks.</div>
      ) : (
        <section className="panel hidden overflow-hidden p-0 md:block">
          <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
            <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Search Results</p>
            <h2 className="mt-2 text-2xl font-bold">{query.sourceCity} to {query.destinationCity}</h2>
            <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
              Review the available trucks below and choose the one that best fits your shipment weight, price, and transporter preference.
            </p>

            <div className="mt-6 grid gap-4 md:grid-cols-4">
              <StatChip label="Source" value={query.sourceCity} />
              <StatChip label="Destination" value={query.destinationCity} />
              <StatChip label="Load Weight" value={`${query.weight} kg`} />
              <StatChip label="Matches" value={`${results.length} trucks`} />
            </div>
          </div>
        </section>
      )}

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {results.map((truck) => (
          <article key={truck.truckId} className="panel overflow-hidden p-0">
            <button
              className="w-full border-b border-slate-200 bg-slate-50 p-4 text-left md:cursor-default"
              onClick={() => setExpandedTruckId((current) => (current === truck.truckId ? null : truck.truckId))}
              type="button"
            >
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="text-xs uppercase tracking-[0.3em] text-accent">{formatTruckType(truck.truckType)}</p>
                  <h3 className="mt-1.5 text-lg font-bold text-ink">{truck.truckNumber}</h3>
                  <p className="mt-1.5 text-sm text-slate-600">{truck.locationCity}</p>
                </div>
                <div className="flex items-start gap-3">
                  <div className="rounded-xl bg-amber-50 px-3 py-2.5 text-right">
                    <p className="text-xs uppercase tracking-[0.2em] text-amber-700">Est. Price</p>
                    <p className="mt-1 text-lg font-bold text-amber-900">Rs {truck.estimatedPrice}</p>
                  </div>
                  <div className="mt-2 md:hidden">
                    <svg
                      aria-hidden="true"
                      className={`h-5 w-5 text-slate-500 transition-transform ${expandedTruckId === truck.truckId ? "rotate-180" : ""}`}
                      viewBox="0 0 20 20"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="1.8"
                    >
                      <path d="m5 7 5 5 5-5" strokeLinecap="round" strokeLinejoin="round" />
                    </svg>
                  </div>
                </div>
              </div>
            </button>

            <div className={`${expandedTruckId === truck.truckId ? "block" : "hidden"} p-4 md:block`}>
              <div className="grid gap-3 sm:grid-cols-2">
                <StatChip label="Capacity" value={`${truck.capacity} kg`} />
                <StatChip label="Distance" value={`${truck.estimatedDistanceKm} km`} />
                <StatChip label="Transporter" value={truck.transporterName || "-"} />
                <StatChip label="Rating" value={truck.transporterRating ?? "New"} />
              </div>

              <div className="mt-4 rounded-2xl border border-slate-200 bg-mist p-3.5">
                <p className="text-xs uppercase tracking-[0.24em] text-slate-500">Transporter Snapshot</p>
                <div className="mt-2.5 flex items-center justify-between gap-3">
                  <div>
                    <p className="text-sm font-semibold text-ink">{truck.transporterName || "Transporter"}</p>
                    <p className="mt-1 text-xs text-slate-500">
                      {truck.transporterVerified ? "Verified transporter" : "Verification pending"}
                    </p>
                  </div>
                  <span className={`rounded-full px-3 py-1.5 text-xs font-semibold ${truck.transporterVerified ? "bg-emerald-100 text-emerald-800" : "bg-slate-200 text-slate-700"}`}>
                    {truck.transporterVerified ? "Verified" : "Standard"}
                  </span>
                </div>
              </div>

              <button
                className="action-button mt-4 w-full"
                onClick={() => handleBookNow(truck)}
                disabled={bookingTruckId === truck.truckId}
              >
                {bookingTruckId === truck.truckId ? "Booking..." : "Book This Truck"}
              </button>
            </div>
          </article>
        ))}
      </div>

      {query && results.length === 0 ? (
        <div className="panel p-6 text-sm text-slate-600">
          No trucks matched this route and weight right now. Try a different city combination or lower shipment weight.
        </div>
      ) : null}
    </PageShell>
  );
}
