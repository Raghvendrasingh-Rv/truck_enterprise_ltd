import api from "./api";

const profileService = {
  async getCustomerProfile() {
    const { data } = await api.get("/users/profile");
    return data.data;
  },

  async updateCustomerProfile(payload) {
    const { data } = await api.put("/users/profile", payload);
    return data.data;
  },

  async getTransporterProfile(id) {
    const { data } = await api.get(`/transporters/${id}`);
    return data.data;
  },

  async updateTransporterProfile(id, payload) {
    const { data } = await api.put(`/transporters/${id}`, payload);
    return data.data;
  },
};

export default profileService;
