import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

export default function TransporterBookingDetailPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [booking, setBooking] = useState(null);

  useEffect(() => {
    bookingService.getBooking(id).then(setBooking).catch(() => setBooking(null));
  }, [id]);

  const runAction = async (action) => {
    if (!booking) return;
    const next = await action(booking.id);
    navigate("/transporter/bookings", { state: { updatedBooking: next } });
  };

  return (
    <PageShell title={`Booking #${id}`} subtitle="Operate booking lifecycle from a focused transporter view.">
      <div className="panel space-y-4 p-6">
        {booking ? (
          <>
            <pre className="overflow-auto text-sm">{JSON.stringify(booking, null, 2)}</pre>
            <div className="flex flex-wrap gap-3">
              <button className="action-button" onClick={() => runAction(bookingService.acceptBooking)}>Accept</button>
              <button className="secondary-button" onClick={() => runAction(bookingService.startBooking)}>Start</button>
              <button className="secondary-button" onClick={() => runAction(bookingService.completeBooking)}>Complete</button>
            </div>
          </>
        ) : "Loading booking..."}
      </div>
    </PageShell>
  );
}
