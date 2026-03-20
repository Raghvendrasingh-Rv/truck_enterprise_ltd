import { Outlet } from "react-router-dom";
import SidebarNav from "../components/SidebarNav";

const links = [
  { label: "Dashboard", to: "/transporter/dashboard" },
  { label: "Trucks", to: "/transporter/trucks" },
  { label: "Bookings", to: "/transporter/bookings" },
  { label: "Profile", to: "/transporter/profile" },
];

export default function TransporterLayout() {
  return (
    <div className="app-shell">
      <div className="mx-auto grid min-h-screen max-w-7xl gap-6 px-4 py-6 md:grid-cols-[260px_1fr] md:px-6">
        <SidebarNav links={links} />
        <main className="panel p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
