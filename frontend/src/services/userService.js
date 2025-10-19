import api from './api';

const userService = {
  // Register user
  register: (userData) => api.post('/api/users', userData),

  // Login (You'll need to implement auth endpoint in backend)
  login: async (email, password) => {
    // For now, fetch user by email and store in localStorage
    const response = await api.get(`/api/users/email/${email}`);
    const user = response.data;
    // In production, verify password on backend
    localStorage.setItem('user', JSON.stringify(user));
    return user;
  },

  // Logout
  logout: () => {
    localStorage.removeItem('user');
  },

  // Get current user
  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem('user') || '{}');
  },

  // Get user by ID
  getUserById: (id) => api.get(`/api/users/${id}`),

  // Update user
  updateUser: (id, userData) => api.put(`/api/users/${id}`, userData),

  // Get all users (Admin)
  getAllUsers: () => api.get('/api/users'),
};

export default userService;