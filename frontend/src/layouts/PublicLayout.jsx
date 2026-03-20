import { Outlet } from "react-router-dom";
import BrandMark from "../components/BrandMark";

export default function PublicLayout() {
  return (
    <div className="app-shell">
      <div className="mx-auto grid min-h-screen max-w-7xl gap-10 px-4 py-8 md:grid-cols-[1.1fr_0.9fr] md:px-6">
        <section className="flex flex-col justify-between rounded-[2rem] bg-ink p-8 text-white shadow-card">
          <BrandMark />
          <div className="space-y-5">
            <p className="text-xs uppercase tracking-[0.35em] text-amber-300">Smart Freight Matching</p>
            <h2 className="max-w-xl text-4xl font-bold leading-tight md:text-5xl">
              Move bookings, trucks, and deliveries through one calm control center.
            </h2>
            <p className="max-w-xl text-sm leading-7 text-slate-300">
              Customer journeys stay clean and price-focused, while transporter operations stay fast, dense, and actionable.
            </p>
          </div>
        </section>
        <section className="flex items-center">
          <div className="panel w-full p-6 md:p-8">
            <Outlet />
          </div>
        </section>
      </div>
    </div>
  );
}
