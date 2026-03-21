import api from "./api";

const notificationService = {
  async getNotifications({ page = 0, size = 20 } = {}) {
    const { data } = await api.get("/notifications", { params: { page, size } });
    return data.data;
  },

  async getUnreadCount() {
    const { data } = await api.get("/notifications/unread-count");
    return data.data;
  },

  async markAsRead(notificationId) {
    const { data } = await api.put(`/notifications/${notificationId}/read`);
    return data.data;
  },

  async markAllAsRead() {
    const { data } = await api.put("/notifications/read-all");
    return data.data;
  },
};

export default notificationService;
