import { Outlet } from "react-router-dom";
import SidebarNav from "../components/SidebarNav";

const links = [
  { label: "Cities", to: "/admin/cities" },
  { label: "Add City", to: "/admin/cities/new" },
];

export default function AdminLayout() {
  return (
    <div className="app-shell">
      <div className="mx-auto grid min-h-screen max-w-7xl gap-6 px-4 py-6 md:grid-cols-[240px_1fr] md:px-6">
        <SidebarNav links={links} />
        <main className="panel p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
