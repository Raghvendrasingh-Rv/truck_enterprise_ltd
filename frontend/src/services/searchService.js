import api from "./api";

const searchService = {
  async getCities() {
    const { data } = await api.get("/cities");
    return data.data;
  },

  async searchTrucks(payload) {
    const { data } = await api.post("/search", payload);
    return data.data;
  },
};

export default searchService;
