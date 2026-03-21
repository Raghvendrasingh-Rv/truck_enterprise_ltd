import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import BookingDetailCard from "../components/BookingDetailCard";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

export default function TransporterBookingDetailPage() {
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

  const runAction = async (action) => {
    if (!booking || submitting) return;

    setSubmitting(true);
    setError("");

    try {
      const next = await action(booking.id);
      navigate("/transporter/bookings", { state: { updatedBooking: next } });
    } catch {
      setError("That action could not be completed. Please try again.");
      setSubmitting(false);
    }
  };

  const actionButtons = booking ? {
    CREATED: (
      <>
        <button className="action-button" disabled={submitting} onClick={() => runAction(bookingService.acceptBooking)}>Accept</button>
        <button className="secondary-button" disabled={submitting} onClick={() => runAction(bookingService.cancelBooking)}>Cancel</button>
      </>
    ),
    ACCEPTED: (
      <button className="action-button" disabled={submitting} onClick={() => runAction(bookingService.startBooking)}>Start</button>
    ),
    IN_TRANSIT: (
      <button className="action-button" disabled={submitting} onClick={() => runAction(bookingService.completeBooking)}>Complete</button>
    ),
  } : {};

  return (
    <PageShell
      title={`Booking #${id}`}
      subtitle="Operate the booking lifecycle from a cleaner transporter operations view."
      actions={booking ? <div className="flex flex-wrap gap-3">{actionButtons[booking.status] ?? null}</div> : null}
    >
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}
      {loading ? <div className="panel p-6">Loading booking details...</div> : null}
      {!loading && booking ? <BookingDetailCard booking={booking} actor="TRANSPORTER" /> : null}
    </PageShell>
  );
}
