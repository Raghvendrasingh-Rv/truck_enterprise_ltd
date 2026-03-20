import { NavLink } from "react-router-dom";
import BrandMark from "./BrandMark";
import { useAuth } from "../hooks/useAuth";

export default function AppHeader({ links }) {
  const { logout } = useAuth();

  return (
    <header className="border-b border-slate-200 bg-white/80 backdrop-blur">
      <div className="mx-auto flex max-w-7xl flex-col gap-4 px-4 py-4 md:flex-row md:items-center md:justify-between md:px-6">
        <BrandMark />
        <nav className="flex flex-wrap gap-2">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) =>
                `rounded-xl px-4 py-2 text-sm font-semibold ${isActive ? "bg-ink text-white" : "bg-mist text-ink"}`
              }
            >
              {link.label}
            </NavLink>
          ))}
          <button className="secondary-button" onClick={logout}>
            Logout
          </button>
        </nav>
      </div>
    </header>
  );
}
