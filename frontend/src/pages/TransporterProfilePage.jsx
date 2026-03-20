import PageShell from "../components/PageShell";
import { useAuth } from "../hooks/useAuth";

export default function TransporterProfilePage() {
  const { auth } = useAuth();
  return (
    <PageShell title="Transporter Profile" subtitle="Business profile summary for your transporter account.">
      <div className="panel p-6">
        <p className="text-lg font-bold">{auth?.companyName || auth?.name}</p>
        <p className="text-sm text-slate-600">{auth?.email}</p>
      </div>
    </PageShell>
  );
}
