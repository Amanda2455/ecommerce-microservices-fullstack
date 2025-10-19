import api from './api';

const orderService = {
  // Create order
  createOrder: (orderData) => api.post('/api/orders', orderData),

  // Get order by ID
  getOrderById: (id) => api.get(`/api/orders/${id}`),

  // Get order by order number
  getOrderByOrderNumber: (orderNumber) => api.get(`/api/orders/order-number/${orderNumber}`),

  // Get all orders (Admin)
  getAllOrders: () => api.get('/api/orders'),

  // Get orders by user ID
  getOrdersByUserId: (userId) => api.get(`/api/orders/user/${userId}`),

  // Get orders by status
  getOrdersByStatus: (status) => api.get(`/api/orders/status/${status}`),

  // Get orders by email
  getOrdersByEmail: (email) => api.get(`/api/orders/email/${email}`),

  // Update order status (Admin)
  updateOrderStatus: (id, statusData) => api.patch(`/api/orders/${id}/status`, statusData),

  // Cancel order
  cancelOrder: (id, cancellationData) => api.post(`/api/orders/${id}/cancel`, cancellationData),

  // Get order status history
  getOrderStatusHistory: (orderId) => api.get(`/api/order-status-history/order/${orderId}`),
};

export default orderService;