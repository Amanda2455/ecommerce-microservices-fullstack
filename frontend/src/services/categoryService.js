import api from './api';

const categoryService = {
  // Get all categories
  getAllCategories: () => api.get('/api/categories'),

  // Get category by ID
  getCategoryById: (id) => api.get(`/api/categories/${id}`),

  // Get category by slug
  getCategoryBySlug: (slug) => api.get(`/api/categories/slug/${slug}`),

  // Get root categories
  getRootCategories: () => api.get('/api/categories/root'),

  // Get active root categories
  getActiveRootCategories: () => api.get('/api/categories/root/active'),

  // Get subcategories
  getSubCategories: (parentId) => api.get(`/api/categories/${parentId}/subcategories`),

  // Get categories by status
  getCategoriesByStatus: (status) => api.get(`/api/categories/status/${status}`),

  // Create category (Admin)
  createCategory: (categoryData) => api.post('/api/categories', categoryData),

  // Update category (Admin)
  updateCategory: (id, categoryData) => api.put(`/api/categories/${id}`, categoryData),

  // Update category status (Admin)
  updateCategoryStatus: (id, status) => 
    api.patch(`/api/categories/${id}/status`, null, { params: { status } }),

  // Delete category (Admin)
  deleteCategory: (id) => api.delete(`/api/categories/${id}`),
};

export default categoryService;