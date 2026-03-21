function Field({ label, value }) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white/80 p-4 shadow-sm">
      <p className="text-xs uppercase tracking-[0.25em] text-slate-500">{label}</p>
      <p className="mt-2 text-sm font-semibold text-ink">{value ?? "-"}</p>
    </div>
  );
}

function InfoStrip({ title, body }) {
  return (
    <div className="rounded-2xl border border-white/60 bg-white/10 p-4 backdrop-blur">
      <p className="text-xs uppercase tracking-[0.25em] text-slate-300">{title}</p>
      <p className="mt-2 text-sm font-medium text-white">{body}</p>
    </div>
  );
}

function formatDateTime(value) {
  if (!value) return "-";

  return new Date(value).toLocaleString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function getStatusClasses(status) {
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
      return "bg-slate-100 text-slate-700";
  }
}

export default function BookingDetailCard({ booking, actor }) {
  const actorCopy = actor === "TRANSPORTER"
    ? "Review customer demand, confirm truck allocation, and move the shipment through each operational milestone."
    : "Track your trip, review the assigned truck and transporter, and stay on top of delivery progress.";

  return (
    <div className="space-y-6">
      <section className="panel overflow-hidden p-0">
        <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
            <div>
              <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Booking #{booking.id}</p>
              <h3 className="mt-2 text-2xl font-bold">
                {booking.source} to {booking.destination}
              </h3>
              <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">{actorCopy}</p>
            </div>

            <div className="flex flex-wrap gap-3">
              <span className={`rounded-full px-4 py-2 text-sm font-semibold ${getStatusClasses(booking.status)}`}>
                {booking.status}
              </span>
              <span className="rounded-full bg-white/15 px-4 py-2 text-sm font-semibold text-white backdrop-blur">
                Rs {booking.price ?? "-"}
              </span>
            </div>
          </div>

          <div className="mt-6 grid gap-4 md:grid-cols-3">
            <InfoStrip title="Created" body={formatDateTime(booking.createdAt)} />
            <InfoStrip title="Last Updated" body={formatDateTime(booking.updatedAt)} />
            <InfoStrip title="Shipment Weight" body={`${booking.weight ?? "-"} kg`} />
          </div>
        </div>

        <div className="grid gap-4 border-t border-slate-200 bg-slate-50 p-6 md:grid-cols-3">
          <Field label="Current Stage" value={booking.status} />
          <Field label="Truck Status" value={booking.truckStatus} />
          <Field label="View Mode" value={actor === "TRANSPORTER" ? "Transporter operations" : "Customer tracking"} />
        </div>
      </section>

      <section className="grid gap-4 lg:grid-cols-[1.2fr_0.8fr]">
        <div className="panel p-6">
          <p className="text-xs uppercase tracking-[0.3em] text-accent">Shipment Route</p>
          <div className="mt-4 grid gap-4 md:grid-cols-[1fr_auto_1fr] md:items-center">
            <div className="rounded-2xl bg-mist p-5">
              <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Pickup City</p>
              <p className="mt-2 text-xl font-bold text-ink">{booking.source}</p>
            </div>
            <div className="mx-auto hidden h-px w-16 bg-slate-300 md:block" />
            <div className="rounded-2xl bg-mist p-5">
              <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Drop City</p>
              <p className="mt-2 text-xl font-bold text-ink">{booking.destination}</p>
            </div>
          </div>
        </div>

        <div className="panel p-6">
          <h4 className="text-lg font-bold text-ink">Commercial Snapshot</h4>
          <div className="mt-4 grid gap-3">
            <Field label="Booking Price" value={`Rs ${booking.price ?? "-"}`} />
            <Field label="Weight" value={`${booking.weight ?? "-"} kg`} />
            <Field label="Booking Id" value={booking.id} />
          </div>
        </div>
      </section>

      <section className="grid gap-4 lg:grid-cols-2">
        <div className="panel p-6">
          <h4 className="text-lg font-bold text-ink">Shipment Info</h4>
          <div className="mt-4 grid gap-3 md:grid-cols-2">
            <Field label="Source" value={booking.source} />
            <Field label="Destination" value={booking.destination} />
            <Field label="Weight" value={`${booking.weight ?? "-"} kg`} />
            <Field label="Booking Status" value={booking.status} />
          </div>
        </div>

        <div className="panel p-6">
          <h4 className="text-lg font-bold text-ink">Truck Info</h4>
          <div className="mt-4 grid gap-3 md:grid-cols-2">
            <Field label="Truck Number" value={booking.truckNumber} />
            <Field label="Truck Type" value={booking.truckType} />
            <Field label="Capacity" value={`${booking.truckCapacityKg ?? "-"} kg`} />
            <Field label="Truck Status" value={booking.truckStatus} />
            <Field label="Current City" value={booking.truckLocationCity} />
            <Field label="Truck Id" value={booking.truckId} />
          </div>
        </div>
      </section>

      <section className="grid gap-4 lg:grid-cols-2">
        <div className="panel p-6">
          <h4 className="text-lg font-bold text-ink">Customer Info</h4>
          <div className="mt-4 grid gap-3 md:grid-cols-2">
            <Field label="Customer Name" value={booking.customerName} />
            <Field label="Customer Email" value={booking.customerEmail} />
            <Field label="Phone Number" value={booking.customerPhone} />
            <Field label="Customer Id" value={booking.customerId} />
          </div>
        </div>

        <div className="panel p-6">
          <h4 className="text-lg font-bold text-ink">Transporter Info</h4>
          <div className="mt-4 grid gap-3 md:grid-cols-2">
            <Field label="Transporter Name" value={booking.transporterName} />
            <Field label="Company" value={booking.transporterCompanyName} />
            <Field label="Email" value={booking.transporterEmail} />
            <Field label="Mobile Number" value={booking.transporterMobileNumber} />
            <Field label="Experience" value={booking.transporterYearsOfExperience != null ? `${booking.transporterYearsOfExperience} years` : "-"} />
            <Field label="Rating" value={booking.transporterRating != null ? booking.transporterRating : "-"} />
            <Field label="Verified" value={booking.transporterVerified == null ? "-" : booking.transporterVerified ? "Yes" : "No"} />
            <Field label="Transporter Id" value={booking.transporterId} />
            <Field label="Address" value={booking.transporterAddress} />
          </div>
        </div>
      </section>
    </div>
  );
}
