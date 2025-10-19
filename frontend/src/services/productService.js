import api from './api';

const productService = {
  // Get all products
  getAllProducts: () => api.get('/api/products'),

  // Get product by ID
  getProductById: (id) => api.get(`/api/products/${id}`),

  // Get product by SKU
  getProductBySku: (sku) => api.get(`/api/products/sku/${sku}`),

  // Get products by category
  getProductsByCategory: (categoryId) => api.get(`/api/products/category/${categoryId}`),

  // Get products by brand
  getProductsByBrand: (brand) => api.get(`/api/products/brand/${brand}`),

  // Get products by status
  getProductsByStatus: (status) => api.get(`/api/products/status/${status}`),

  // Search products
  searchProducts: (keyword) => api.get(`/api/products/search`, { params: { keyword } }),

  // Get products by price range
  getProductsByPriceRange: (minPrice, maxPrice) => 
    api.get('/api/products/price-range', { params: { minPrice, maxPrice } }),

  // Get featured products
  getFeaturedProducts: () => api.get('/api/products/featured'),

  // Get best sellers
  getBestSellers: () => api.get('/api/products/best-sellers'),

  // Get new arrivals
  getNewArrivals: () => api.get('/api/products/new-arrivals'),

  // Create product (Admin)
  createProduct: (productData) => api.post('/api/products', productData),

  // Update product (Admin)
  updateProduct: (id, productData) => api.put(`/api/products/${id}`, productData),

  // Update product status (Admin)
  updateProductStatus: (id, status) => 
    api.patch(`/api/products/${id}/status`, null, { params: { status } }),

  // Delete product (Admin)
  deleteProduct: (id) => api.delete(`/api/products/${id}`),
};

export default productService;