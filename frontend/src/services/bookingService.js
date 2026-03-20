import api from "./api";

const bookingService = {
  async createBooking(payload) {
    const { data } = await api.post("/bookings", payload);
    return data.data;
  },

  async getCustomerBookings() {
    const { data } = await api.get("/bookings/customer/me");
    return data.data;
  },

  async getTransporterBookings() {
    const { data } = await api.get("/bookings/transporter/me");
    return data.data;
  },

  async getBooking(bookingId) {
    const { data } = await api.get(`/bookings/${bookingId}`);
    return data.data;
  },

  async acceptBooking(bookingId) {
    const { data } = await api.put(`/bookings/${bookingId}/accept`);
    return data.data;
  },

  async startBooking(bookingId) {
    const { data } = await api.put(`/bookings/${bookingId}/start`);
    return data.data;
  },

  async completeBooking(bookingId) {
    const { data } = await api.put(`/bookings/${bookingId}/complete`);
    return data.data;
  },

  async cancelBooking(bookingId) {
    const { data } = await api.put(`/bookings/${bookingId}/cancel`);
    return data.data;
  },
};

export default bookingService;
