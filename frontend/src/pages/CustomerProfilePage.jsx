import { useEffect, useState } from "react";
import PageShell from "../components/PageShell";
import { useAuth } from "../hooks/useAuth";
import profileService from "../services/profileService";

function ProfileStat({ label, value }) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white/75 p-4 shadow-sm">
      <p className="text-xs uppercase tracking-[0.25em] text-slate-500">{label}</p>
      <p className="mt-2 text-sm font-semibold text-ink">{value ?? "-"}</p>
    </div>
  );
}

function formatDate(value) {
  if (!value) return "-";
  return new Date(value).toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

export default function CustomerProfilePage() {
  const { auth, syncAuth } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({ name: "", phone: "" });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [editing, setEditing] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    profileService.getCustomerProfile()
      .then((data) => {
        setProfile(data);
        setForm({
          name: data.name ?? "",
          phone: data.phone ?? "",
        });
      })
      .catch(() => setError("We could not load your profile right now."))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError("");
    setMessage("");

    try {
      const updated = await profileService.updateCustomerProfile(form);
      setProfile(updated);
      syncAuth({ name: updated.name, email: updated.email });
      setMessage("Profile updated successfully.");
      setEditing(false);
    } catch {
      setError("We could not save your profile right now.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <PageShell title="Customer Profile" subtitle="Keep your account details fresh so bookings and notifications stay accurate.">
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}
      {message ? <div className="panel border border-emerald-200 bg-emerald-50 p-4 text-sm text-emerald-700">{message}</div> : null}

      {loading ? (
        <div className="panel p-6">Loading profile...</div>
      ) : (
        <div className="grid gap-6 xl:grid-cols-[1.15fr_0.85fr]">
          <section className="panel overflow-hidden p-0">
            <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.22),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
              <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Customer Account</p>
              <h2 className="mt-2 text-2xl font-bold">{profile?.name || auth?.name}</h2>
              <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
                Manage the personal details tied to your bookings, notifications, and shipment records.
              </p>
            </div>

            <div className="grid gap-4 bg-slate-50 p-6 md:grid-cols-2">
              <ProfileStat label="Email Address" value={profile?.email || auth?.email} />
              <ProfileStat label="Phone Number" value={profile?.phone} />
              <ProfileStat label="Role" value={profile?.role} />
              <ProfileStat label="Joined On" value={formatDate(profile?.createdAt)} />
            </div>
          </section>

          <section className="panel p-6">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs uppercase tracking-[0.28em] text-accent">{editing ? "Update Profile" : "Profile Actions"}</p>
                <h3 className="mt-2 text-xl font-bold text-ink">{editing ? "Personal details" : "Manage your account"}</h3>
                <p className="mt-2 text-sm leading-6 text-slate-600">
                  {editing
                    ? "Update the information that customers, support teams, and delivery workflows rely on."
                    : "Review your account details and open edit mode only when you want to make changes."}
                </p>
              </div>

              {!editing ? (
                <button className="action-button" onClick={() => setEditing(true)} type="button">
                  Edit Profile
                </button>
              ) : null}
            </div>

            {editing ? (
              <form className="mt-6 grid gap-4" onSubmit={handleSubmit}>
                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Name</label>
                  <input
                    className="field"
                    value={form.name}
                    onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))}
                    placeholder="Enter your full name"
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Phone Number</label>
                  <input
                    className="field"
                    value={form.phone}
                    onChange={(event) => setForm((current) => ({ ...current, phone: event.target.value }))}
                    placeholder="Enter your phone number"
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Email</label>
                  <input className="field bg-slate-50" value={profile?.email || auth?.email || ""} disabled readOnly />
                </div>

                <div className="flex flex-col gap-3 pt-2 sm:flex-row">
                  <button className="action-button flex-1" disabled={saving} type="submit">
                    {saving ? "Saving..." : "Save Changes"}
                  </button>
                  <button
                    className="secondary-button flex-1"
                    onClick={() => {
                      setEditing(false);
                      setForm({
                        name: profile?.name ?? "",
                        phone: profile?.phone ?? "",
                      });
                    }}
                    type="button"
                  >
                    Cancel
                  </button>
                </div>
              </form>
            ) : null}
          </section>
        </div>
      )}
    </PageShell>
  );
}
