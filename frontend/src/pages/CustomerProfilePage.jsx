import PageShell from "../components/PageShell";
import { useAuth } from "../hooks/useAuth";

export default function CustomerProfilePage() {
  const { auth } = useAuth();
  return (
    <PageShell title="Customer Profile" subtitle="Account summary for the logged-in customer.">
      <div className="panel p-6">
        <p className="font-semibold">{auth?.name}</p>
        <p className="text-sm text-slate-600">{auth?.email}</p>
      </div>
    </PageShell>
  );
}
