import api from "./api";

const truckService = {
  async getTransporterTrucks() {
    throw new Error("Use getTransporterTrucksById with current transporter profile id.");
  },

  async getTransporterTrucksById(transporterId) {
    const { data } = await api.get(`/trucks/transporter/${transporterId}`);
    return data.data;
  },

  async getTruckById(truckId) {
    const { data } = await api.get(`/trucks/${truckId}`);
    return data.data;
  },

  async addTruck(payload) {
    const { data } = await api.post("/trucks", payload);
    return data.data;
  },

  async updateTruck(truckId, payload) {
    const { data } = await api.put(`/trucks/${truckId}`, payload);
    return data.data;
  },

  async deleteTruck(truckId) {
    const { data } = await api.delete(`/trucks/${truckId}`);
    return data.data;
  },
};

export default truckService;
