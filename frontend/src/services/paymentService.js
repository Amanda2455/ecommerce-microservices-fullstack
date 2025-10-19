import api from './api';

const paymentService = {
  // Create payment
  createPayment: (paymentData) => api.post('/api/payments', paymentData),

  // Process payment
  processPayment: (id) => api.post(`/api/payments/${id}/process`),

  // Get payment by ID
  getPaymentById: (id) => api.get(`/api/payments/${id}`),

  // Get payment by order ID
  getPaymentByOrderId: (orderId) => api.get(`/api/payments/order/${orderId}`),

  // Confirm COD payment
  confirmCODPayment: (id) => api.post(`/api/payments/${id}/confirm-cod`),

  // Create refund
  createRefund: (refundData) => api.post('/api/refunds', refundData),

  // Get refunds by order ID
  getRefundsByOrderId: (orderId) => api.get(`/api/refunds/order/${orderId}`),
};

export default paymentService;