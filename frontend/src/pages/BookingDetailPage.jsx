import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import PageShell from "../components/PageShell";
import bookingService from "../services/bookingService";

export default function BookingDetailPage() {
  const { id } = useParams();
  const [booking, setBooking] = useState(null);

  useEffect(() => {
    bookingService.getBooking(id).then(setBooking).catch(() => setBooking(null));
  }, [id]);

  return (
    <PageShell title={`Booking #${id}`} subtitle="Detailed booking status view for customer or transporter.">
      <div className="panel p-6">
        {booking ? <pre className="overflow-auto text-sm">{JSON.stringify(booking, null, 2)}</pre> : "Loading booking details..."}
      </div>
    </PageShell>
  );
}
