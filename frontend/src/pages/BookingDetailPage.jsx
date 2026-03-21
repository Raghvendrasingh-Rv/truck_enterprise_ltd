import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import BookingDetailCard from "../components/BookingDetailCard";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

export default function BookingDetailPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [booking, setBooking] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    setError("");

    bookingService.getBooking(id)
      .then(setBooking)
      .catch(() => {
        setBooking(null);
        setError("We could not load this booking right now.");
      })
      .finally(() => setLoading(false));
  }, [id]);

  const handleCancel = async () => {
    if (!booking || submitting || booking.status === "DELIVERED" || booking.status === "CANCELLED") {
      return;
    }

    setSubmitting(true);
    setError("");

    try {
      const updated = await bookingService.cancelBooking(booking.id);
      navigate("/customer/bookings", { state: { updatedBooking: updated } });
    } catch {
      setError("We could not cancel this booking right now.");
      setSubmitting(false);
    }
  };

  return (
    <PageShell
      title={`Booking #${id}`}
      subtitle="Track every field of the booking, truck, and transporter in one clean customer view."
      actions={
        booking && booking.status !== "DELIVERED" && booking.status !== "CANCELLED" ? (
          <button className="secondary-button" disabled={submitting} onClick={handleCancel}>
            Cancel Booking
          </button>
        ) : null
      }
    >
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}
      {loading ? <div className="panel p-6">Loading booking details...</div> : null}
      {!loading && booking ? <BookingDetailCard booking={booking} actor="CUSTOMER" /> : null}
    </PageShell>
  );
}
