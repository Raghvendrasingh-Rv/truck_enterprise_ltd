import { NavLink, useNavigate } from "react-router-dom";
import BrandMark from "./BrandMark";
import NotificationBell from "./NotificationBell";
import { useAuth } from "../hooks/useAuth";

export default function AppHeader({ links }) {
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <header className="sticky top-0 z-30 border-b border-slate-200/60 bg-[linear-gradient(135deg,rgba(255,255,255,0.94),rgba(245,247,250,0.92)),radial-gradient(circle_at_top_left,rgba(242,113,33,0.16),transparent_28%),radial-gradient(circle_at_top_right,rgba(15,23,42,0.08),transparent_26%)] backdrop-blur-xl">
      <div className="mx-auto max-w-7xl px-4 py-3 md:px-6">
        <div className="rounded-[28px] border border-white/60 bg-white/35 px-4 py-4 shadow-[0_18px_50px_rgba(15,23,42,0.08)] backdrop-blur">
          <div className="flex items-center justify-between gap-4">
            <div className="min-w-0">
              <BrandMark />
            </div>

            <div className="flex items-center gap-2 md:gap-3">
              <NotificationBell />
              <button className="secondary-button hidden sm:inline-flex" onClick={handleLogout}>
                Logout
              </button>
            </div>
          </div>

          <div className="mt-4 flex items-center gap-3">
            <nav className="flex-1 overflow-x-auto pb-1 [-ms-overflow-style:none] [scrollbar-width:none] [&::-webkit-scrollbar]:hidden">
              <div className="flex min-w-max gap-2">
                {links.map((link) => (
                  <NavLink
                    key={link.to}
                    to={link.to}
                    className={({ isActive }) =>
                      [
                        "rounded-full px-4 py-2.5 text-sm font-semibold transition",
                        isActive
                          ? "bg-[linear-gradient(135deg,#0f172a,#1e293b)] text-white shadow-lg shadow-slate-900/20"
                          : "border border-white/70 bg-white/65 text-slate-700 hover:border-slate-200 hover:bg-white/90",
                      ].join(" ")
                    }
                  >
                    {link.label}
                  </NavLink>
                ))}
              </div>
            </nav>
          </div>
        </div>
      </div>
    </header>
  );
}
