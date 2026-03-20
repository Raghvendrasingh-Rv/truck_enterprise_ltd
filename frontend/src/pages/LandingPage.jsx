import { Link } from "react-router-dom";

export default function LandingPage() {
  return (
    <div className="space-y-6">
      <div>
        <p className="text-xs uppercase tracking-[0.35em] text-accent">Truck Booking Platform</p>
        <h2 className="mt-3 text-3xl font-bold text-ink">Choose how you want to enter the platform.</h2>
      </div>
      <div className="grid gap-4 md:grid-cols-2">
        <Link to="/register" className="panel p-5 transition hover:-translate-y-1">
          <p className="text-sm font-semibold text-accent">Customer</p>
          <h3 className="mt-2 text-xl font-bold">Search trucks and manage bookings</h3>
          <p className="mt-2 text-sm text-slate-600">Ideal for customers who want to compare trucks and place freight bookings.</p>
        </Link>
        <Link to="/transporter/register" className="panel p-5 transition hover:-translate-y-1">
          <p className="text-sm font-semibold text-accent">Transporter</p>
          <h3 className="mt-2 text-xl font-bold">Run your fleet and deliveries</h3>
          <p className="mt-2 text-sm text-slate-600">Ideal for operators who manage trucks, incoming requests, and delivery status.</p>
        </Link>
      </div>
      <div className="flex flex-wrap gap-3">
        <Link className="action-button" to="/login">
          Customer Login
        </Link>
        <Link className="secondary-button" to="/transporter/login">
          Transporter Login
        </Link>
      </div>
    </div>
  );
}
