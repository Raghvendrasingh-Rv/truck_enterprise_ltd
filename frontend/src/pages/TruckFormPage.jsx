import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import PageShell from "../components/PageShell";
import truckService from "../services/truckService";

const baseForm = { truckNumber: "", truckType: "BOX_TRUCK", capacityKg: "", locationCity: "" };
const truckTypes = ["FLATBED", "REFRIGERATED", "TANKER", "BOX_TRUCK", "PICKUP", "DUMP_TRUCK", "AUTO_CARRIER"];

export default function TruckFormPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState(baseForm);

  useEffect(() => {
    if (!id) return;
    truckService.getTransporterTrucksById().catch(() => {});
  }, [id]);

  const onSubmit = async (event) => {
    event.preventDefault();
    const payload = { ...form, capacityKg: Number(form.capacityKg) };
    if (id) {
      await truckService.updateTruck(id, payload);
    } else {
      await truckService.addTruck(payload);
    }
    navigate("/transporter/trucks");
  };

  return (
    <PageShell title={id ? "Edit Truck" : "Add Truck"} subtitle="Keep truck forms concise and easy to operate from mobile or desktop.">
      <form className="panel grid gap-4 p-5 md:grid-cols-2" onSubmit={onSubmit}>
        <input className="field" placeholder="Truck number" value={form.truckNumber} onChange={(e) => setForm({ ...form, truckNumber: e.target.value })} />
        <select className="field" value={form.truckType} onChange={(e) => setForm({ ...form, truckType: e.target.value })}>
          {truckTypes.map((truckType) => <option key={truckType} value={truckType}>{truckType}</option>)}
        </select>
        <input className="field" type="number" placeholder="Capacity (kg)" value={form.capacityKg} onChange={(e) => setForm({ ...form, capacityKg: e.target.value })} />
        <input className="field" placeholder="Location city" value={form.locationCity} onChange={(e) => setForm({ ...form, locationCity: e.target.value })} />
        <button className="action-button md:col-span-2" type="submit">{id ? "Update Truck" : "Add Truck"}</button>
      </form>
    </PageShell>
  );
}
