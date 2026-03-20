import { useLocation, useNavigate } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";
import { useState } from "react";

export default function SearchResultsPage() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const results = state?.results || [];
  const query = state?.query;
  const [bookingTruckId, setBookingTruckId] = useState(null);

  const handleBookNow = async (truck) => {
    if (!query) {
      return;
    }

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
    <PageShell title="Search Results" subtitle="Compare truck type, capacity, transporter, and estimated price.">
      {!query ? <div className="panel p-6 text-sm text-slate-600">Run a search first to view results.</div> : null}
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {results.map((truck) => (
          <article key={truck.truckId} className="panel p-5">
            <div className="flex items-start justify-between">
              <div>
                <p className="text-xs uppercase tracking-[0.3em] text-accent">{truck.truckType}</p>
                <h3 className="mt-2 text-xl font-bold">{truck.truckNumber}</h3>
              </div>
              <div className="rounded-xl bg-mist px-3 py-2 text-right">
                <p className="text-xs text-slate-500">Est. price</p>
                <p className="text-lg font-bold">Rs {truck.estimatedPrice}</p>
              </div>
            </div>
            <div className="mt-4 space-y-2 text-sm text-slate-600">
              <p>Capacity: {truck.capacity} kg</p>
              <p>Transporter: {truck.transporterName}</p>
              <p>Rating: {truck.transporterRating ?? "New"}</p>
              <p>Distance: {truck.estimatedDistanceKm} km</p>
            </div>
            <button
              className="action-button mt-5 w-full"
              onClick={() => handleBookNow(truck)}
              disabled={bookingTruckId === truck.truckId}
            >
              {bookingTruckId === truck.truckId ? "Booking..." : "Book Now"}
            </button>
          </article>
        ))}
      </div>
    </PageShell>
  );
}
