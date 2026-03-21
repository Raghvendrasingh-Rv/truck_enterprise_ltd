import { useEffect, useMemo, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import notificationService from "../services/notificationService";

function formatNotificationDateTime(value) {
  const date = new Date(value);
  return date.toLocaleString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function resolveBookingPath(notification) {
  if (notification.recipientType === "TRANSPORTER") {
    return `/transporter/bookings/${notification.bookingId}`;
  }

  return `/customer/bookings/${notification.bookingId}`;
}

export default function NotificationBell() {
  const { role } = useAuth();
  const containerRef = useRef(null);
  const [open, setOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([
      notificationService.getNotifications({ page: 0, size: 5 }),
      notificationService.getUnreadCount(),
    ])
      .then(([items, unread]) => {
        setNotifications(items);
        setUnreadCount(unread);
      })
      .catch(() => setError("Unable to load notifications"));
  }, []);

  useEffect(() => {
    if (!open) return undefined;

    const handlePointerDown = (event) => {
      if (!containerRef.current?.contains(event.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handlePointerDown);
    document.addEventListener("touchstart", handlePointerDown);

    return () => {
      document.removeEventListener("mousedown", handlePointerDown);
      document.removeEventListener("touchstart", handlePointerDown);
    };
  }, [open]);

  const handleRead = async (notificationId) => {
    try {
      const updated = await notificationService.markAsRead(notificationId);
      setNotifications((current) =>
        current.map((notification) => (notification.id === updated.id ? updated : notification))
      );
      setUnreadCount((current) => Math.max(0, current - 1));
    } catch {
      setError("Unable to update notification");
    }
  };

  const handleReadAll = async () => {
    try {
      await notificationService.markAllAsRead();
      setNotifications((current) => current.map((notification) => ({ ...notification, read: true })));
      setUnreadCount(0);
    } catch {
      setError("Unable to update notifications");
    }
  };

  const historyPath = useMemo(() => {
    return role === "TRANSPORTER" ? "/transporter/notifications" : "/customer/notifications";
  }, [role]);

  return (
    <div className="relative" ref={containerRef}>
      <button
        aria-label="Open notifications"
        className="relative inline-flex h-11 w-11 items-center justify-center rounded-2xl border border-slate-300 bg-white text-ink transition hover:bg-slate-50"
        onClick={() => setOpen((value) => !value)}
      >
        <svg
          aria-hidden="true"
          className="h-5 w-5"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="1.8"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M15 17h5l-1.4-1.4A2 2 0 0 1 18 14.2V11a6 6 0 1 0-12 0v3.2a2 2 0 0 1-.6 1.4L4 17h5" />
          <path d="M10 21a2 2 0 0 0 4 0" />
        </svg>
        {unreadCount > 0 ? (
          <span className="absolute -right-2 -top-2 flex h-6 min-w-6 items-center justify-center rounded-full bg-accent px-1 text-xs font-bold text-white">
            {unreadCount}
          </span>
        ) : null}
      </button>

      {open ? (
        <div className="fixed inset-x-4 top-20 z-20 rounded-2xl border border-slate-200 bg-white p-3 shadow-card sm:absolute sm:right-0 sm:left-auto sm:top-full sm:mt-3 sm:w-[24rem]">
          <div className="mb-3 flex items-center justify-between px-2">
            <h3 className="text-sm font-bold text-ink">Recent notifications</h3>
            <div className="flex items-center gap-3">
              <span className="text-xs text-slate-500">{unreadCount} unread</span>
              {unreadCount > 0 ? (
                <button className="text-xs font-semibold text-accent" onClick={handleReadAll}>
                  Mark all read
                </button>
              ) : null}
            </div>
          </div>
          {error ? <p className="px-2 text-sm text-red-600">{error}</p> : null}
          <div className="max-h-96 space-y-2 overflow-auto">
            {notifications.length === 0 ? (
              <div className="rounded-xl bg-mist px-3 py-4 text-sm text-slate-600">No notifications yet.</div>
            ) : (
              notifications.map((notification) => (
                <div
                  key={notification.id}
                  className={`rounded-xl border p-3 ${notification.read ? "border-slate-200 bg-white" : "border-amber-200 bg-amber-50/70"}`}
                >
                  <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                    <div className="space-y-1">
                      <p className="text-sm font-semibold text-ink">{notification.title}</p>
                      <p className="text-sm text-slate-600">{notification.message}</p>
                      <p className="text-xs uppercase tracking-[0.25em] text-slate-400">{notification.type}</p>
                    </div>
                    {!notification.read ? (
                      <button className="text-left text-xs font-semibold text-accent sm:text-right" onClick={() => handleRead(notification.id)}>
                        Mark read
                      </button>
                    ) : null}
                  </div>
                  <div className="mt-3 flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
                    <span className="text-xs text-slate-400">
                      {formatNotificationDateTime(notification.createdAt)}
                    </span>
                    <Link className="text-sm font-semibold text-ink" to={resolveBookingPath(notification)} onClick={() => setOpen(false)}>
                      Open booking
                    </Link>
                  </div>
                </div>
              ))
            )}
          </div>
          <div className="mt-3 border-t border-slate-200 pt-3 text-right">
            <Link className="text-sm font-semibold text-ink" to={historyPath} onClick={() => setOpen(false)}>
              View all notifications
            </Link>
          </div>
        </div>
      ) : null}
    </div>
  );
}
