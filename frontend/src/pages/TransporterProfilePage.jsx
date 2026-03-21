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

export default function TransporterProfilePage() {
  const { auth, syncAuth } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({
    companyName: "",
    name: "",
    email: "",
    mobileNumber: "",
    yearsOfExperience: "",
    address: "",
  });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [editing, setEditing] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (!auth?.profileId) {
      setLoading(false);
      return;
    }

    setLoading(true);
    profileService.getTransporterProfile(auth.profileId)
      .then((data) => {
        setProfile(data);
        setForm({
          companyName: data.companyName ?? "",
          name: data.name ?? "",
          email: data.email ?? "",
          mobileNumber: data.mobileNumber ?? "",
          yearsOfExperience: data.yearsOfExperience ?? "",
          address: data.address ?? "",
        });
      })
      .catch(() => setError("We could not load your transporter profile right now."))
      .finally(() => setLoading(false));
  }, [auth?.profileId]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!auth?.profileId) return;

    setSaving(true);
    setError("");
    setMessage("");

    try {
      const payload = {
        ...form,
        yearsOfExperience: Number(form.yearsOfExperience),
      };
      const updated = await profileService.updateTransporterProfile(auth.profileId, payload);
      setProfile(updated);
      setForm({
        companyName: updated.companyName ?? "",
        name: updated.name ?? "",
        email: updated.email ?? "",
        mobileNumber: updated.mobileNumber ?? "",
        yearsOfExperience: updated.yearsOfExperience ?? "",
        address: updated.address ?? "",
      });
      syncAuth({
        name: updated.name,
        email: updated.email,
        companyName: updated.companyName,
      });
      setMessage("Transporter profile updated successfully.");
      setEditing(false);
    } catch {
      setError("We could not save your transporter profile right now.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <PageShell title="Transporter Profile" subtitle="Manage the business identity that customers see during truck search and booking fulfillment.">
      {error ? <div className="panel border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">{error}</div> : null}
      {message ? <div className="panel border border-emerald-200 bg-emerald-50 p-4 text-sm text-emerald-700">{message}</div> : null}

      {loading ? (
        <div className="panel p-6">Loading transporter profile...</div>
      ) : (
        <div className="grid gap-6 xl:grid-cols-[1.1fr_0.9fr]">
          <section className="panel overflow-hidden p-0">
            <div className="bg-[radial-gradient(circle_at_top_left,_rgba(242,113,33,0.24),_transparent_32%),linear-gradient(135deg,rgba(15,23,42,1),rgba(30,41,59,0.95))] p-6 text-white">
              <p className="text-xs uppercase tracking-[0.3em] text-orange-200">Transporter Identity</p>
              <h2 className="mt-2 text-2xl font-bold">{profile?.companyName || auth?.companyName}</h2>
              <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200">
                Keep your transporter details up to date so customers trust the listing and operations stay accurate.
              </p>
            </div>

            <div className="grid gap-4 bg-slate-50 p-6 md:grid-cols-2">
              <ProfileStat label="Business Contact" value={profile?.name} />
              <ProfileStat label="Email Address" value={profile?.email} />
              <ProfileStat label="Mobile Number" value={profile?.mobileNumber} />
              <ProfileStat label="Experience" value={profile?.yearsOfExperience != null ? `${profile.yearsOfExperience} years` : "-"} />
              <ProfileStat label="Rating" value={profile?.rating != null ? profile.rating : "-"} />
              <ProfileStat label="Verified" value={profile?.verified == null ? "-" : profile.verified ? "Yes" : "No"} />
              <div className="rounded-2xl border border-slate-200 bg-white/75 p-4 shadow-sm md:col-span-2">
                <p className="text-xs uppercase tracking-[0.25em] text-slate-500">Business Address</p>
                <p className="mt-2 text-sm font-semibold leading-6 text-ink">{profile?.address ?? "-"}</p>
              </div>
              <ProfileStat label="Assigned Trucks" value={profile?.truckIds?.length ?? 0} />
              <ProfileStat label="Transporter Id" value={profile?.id} />
            </div>
          </section>

          <section className="panel p-6">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs uppercase tracking-[0.28em] text-accent">{editing ? "Update Profile" : "Profile Actions"}</p>
                <h3 className="mt-2 text-xl font-bold text-ink">{editing ? "Business details" : "Manage transporter identity"}</h3>
                <p className="mt-2 text-sm leading-6 text-slate-600">
                  {editing
                    ? "Update the transporter information shown across truck listings, booking details, and operations views."
                    : "Open edit mode only when you need to revise company, contact, or operations details."}
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
                  <label className="mb-2 block text-sm font-semibold text-ink">Company Name</label>
                  <input className="field" value={form.companyName} onChange={(event) => setForm((current) => ({ ...current, companyName: event.target.value }))} />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Contact Name</label>
                  <input className="field" value={form.name} onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))} />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Email</label>
                  <input className="field" value={form.email} onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))} />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Mobile Number</label>
                  <input className="field" value={form.mobileNumber} onChange={(event) => setForm((current) => ({ ...current, mobileNumber: event.target.value }))} />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Years of Experience</label>
                  <input
                    className="field"
                    min="0"
                    type="number"
                    value={form.yearsOfExperience}
                    onChange={(event) => setForm((current) => ({ ...current, yearsOfExperience: event.target.value }))}
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-semibold text-ink">Business Address</label>
                  <textarea
                    className="field min-h-28 resize-y"
                    value={form.address}
                    onChange={(event) => setForm((current) => ({ ...current, address: event.target.value }))}
                  />
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
                        companyName: profile?.companyName ?? "",
                        name: profile?.name ?? "",
                        email: profile?.email ?? "",
                        mobileNumber: profile?.mobileNumber ?? "",
                        yearsOfExperience: profile?.yearsOfExperience ?? "",
                        address: profile?.address ?? "",
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
