import { Outlet } from "react-router-dom";
import AppHeader from "../components/AppHeader";

const links = [
  { label: "Search", to: "/customer/search" },
  { label: "Bookings", to: "/customer/bookings" },
  { label: "Profile", to: "/customer/profile" },
];

export default function CustomerLayout() {
  return (
    <div className="app-shell">
      <AppHeader links={links} modeLabel="Customer Workspace" />
      <main className="mx-auto max-w-7xl px-4 py-6 md:px-6">
        <Outlet />
      </main>
    </div>
  );
}
