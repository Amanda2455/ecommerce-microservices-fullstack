import api from './api';

const inventoryService = {
  // Check stock availability
  checkStockAvailability: (productId, quantity) => 
    api.get('/api/inventory/check-availability', { params: { productId, quantity } }),

  // Get inventory by product ID
  getInventoryByProductId: (productId) => api.get(`/api/inventory/product/${productId}`),

  // Get all inventory
  getAllInventory: () => api.get('/api/inventory'),

  // Get low stock items
  getLowStockItems: () => api.get('/api/inventory/low-stock'),

  // Get out of stock items
  getOutOfStockItems: () => api.get('/api/inventory/out-of-stock'),

  // Add stock (Admin)
  addStock: (id, adjustmentData) => api.post(`/api/inventory/${id}/add-stock`, adjustmentData),

  // Remove stock (Admin)
  removeStock: (id, adjustmentData) => api.post(`/api/inventory/${id}/remove-stock`, adjustmentData),

  // Reserve stock (Internal)
  reserveStock: (productId, quantity, orderId) => 
    api.post('/api/inventory/reserve', null, { params: { productId, quantity, orderId } }),

  // Release reserved stock (Internal)
  releaseReservedStock: (productId, quantity, orderId) => 
    api.post('/api/inventory/release', null, { params: { productId, quantity, orderId } }),
};

export default inventoryService;