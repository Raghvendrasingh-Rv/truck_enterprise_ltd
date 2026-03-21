import { Outlet } from "react-router-dom";
import AppHeader from "../components/AppHeader";

const links = [
  { label: "Dashboard", to: "/transporter/dashboard" },
  { label: "Trucks", to: "/transporter/trucks" },
  { label: "Bookings", to: "/transporter/bookings" },
  { label: "Profile", to: "/transporter/profile" },
];

export default function TransporterLayout() {
  return (
    <div className="app-shell">
      <AppHeader links={links} modeLabel="Transporter Operations" />
      <div className="mx-auto max-w-7xl px-4 py-6 md:px-6">
        <main className="panel p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
