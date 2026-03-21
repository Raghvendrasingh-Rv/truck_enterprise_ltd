import { NavLink } from "react-router-dom";
import BrandMark from "./BrandMark";
import NotificationBell from "./NotificationBell";
import { useAuth } from "../hooks/useAuth";

export default function SidebarNav({ links }) {
  const { logout } = useAuth();

  return (
    <aside className="panel h-full p-4">
      <div className="mb-8">
        <BrandMark />
      </div>
      <nav className="flex flex-col gap-2">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `rounded-xl px-4 py-3 text-sm font-semibold ${isActive ? "bg-ink text-white" : "text-ink hover:bg-mist"}`
            }
          >
            {link.label}
          </NavLink>
        ))}
      </nav>
      <div className="mt-6">
        <NotificationBell />
      </div>
      <button className="secondary-button mt-8 w-full" onClick={logout}>
        Logout
      </button>
    </aside>
  );
}
