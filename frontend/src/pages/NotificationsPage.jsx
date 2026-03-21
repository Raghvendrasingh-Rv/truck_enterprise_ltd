import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import PageShell from "../components/PageShell";
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

function resolveBookingPath(notification, mode) {
  return mode === "TRANSPORTER"
    ? `/transporter/bookings/${notification.bookingId}`
    : `/customer/bookings/${notification.bookingId}`;
}

export default function NotificationsPage({ mode }) {
  const [notifications, setNotifications] = useState([]);
  const [page, setPage] = useState(0);
  const [error, setError] = useState("");
  const pageSize = 20;

  useEffect(() => {
    notificationService
      .getNotifications({ page, size: pageSize })
      .then(setNotifications)
      .catch(() => setError("Unable to load notifications"));
  }, [page]);

  const handleRead = async (notificationId) => {
    try {
      const updated = await notificationService.markAsRead(notificationId);
      setNotifications((current) =>
        current.map((notification) => (notification.id === updated.id ? updated : notification))
      );
    } catch (err) {
      setError("Unable to update notification");
    }
  };

  return (
    <PageShell title="Notifications" subtitle="Review your recent booking events and open the related booking detail.">
      {error ? <div className="panel p-4 text-sm text-red-600">{error}</div> : null}
      <div className="grid gap-4">
        {notifications.map((notification) => (
          <div
            key={notification.id}
            className={`panel p-5 ${notification.read ? "" : "ring-1 ring-amber-200"}`}
          >
            <div className="flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
              <div>
                <p className="text-sm font-semibold text-ink">{notification.title}</p>
                <p className="mt-1 text-sm text-slate-600">{notification.message}</p>
                <p className="mt-2 text-xs uppercase tracking-[0.25em] text-slate-400">{notification.type}</p>
                <p className="mt-2 text-xs text-slate-500">{formatNotificationDateTime(notification.createdAt)}</p>
              </div>
              <div className="flex flex-wrap items-center gap-3">
                {!notification.read ? (
                  <button className="secondary-button" onClick={() => handleRead(notification.id)}>
                    Mark read
                  </button>
                ) : null}
                <Link className="action-button" to={resolveBookingPath(notification, mode)}>
                  Open booking
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
      <div className="flex items-center justify-between">
        <button className="secondary-button" disabled={page === 0} onClick={() => setPage((current) => Math.max(0, current - 1))}>
          Previous
        </button>
        <span className="text-sm text-slate-500">Page {page + 1}</span>
        <button className="secondary-button" onClick={() => setPage((current) => current + 1)}>
          Next
        </button>
      </div>
    </PageShell>
  );
}
