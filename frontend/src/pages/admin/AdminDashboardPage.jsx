import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import AdminSidebar from '../../components/admin/AdminSidebar';
import Dashboard from '../../components/admin/Dashboard';
import ProductManagement from '../../components/admin/ProductManagement';
import CategoryManagement from '../../components/admin/CategoryManagement';
import OrderManagement from '../../components/admin/OrderManagement';
import InventoryManagement from '../../components/admin/InventoryManagement';
import UserManagement from '../../components/admin/UserManagement';

const AdminDashboardPage = () => {
  const { isAdmin } = useAuth();

  // Redirect if not admin
  if (!isAdmin()) {
    return <Navigate to="/" />;
  }

  return (
    <div className="flex min-h-screen bg-gray-100">
      {/* Sidebar */}
      <AdminSidebar />

      {/* Main Content */}
      <div className="flex-1">
        <Routes>
          <Route index element={<Dashboard />} />
          <Route path="products" element={<ProductManagement />} />
          <Route path="categories" element={<CategoryManagement />} />
          <Route path="orders" element={<OrderManagement />} />
          <Route path="inventory" element={<InventoryManagement />} />
          <Route path="users" element={<UserManagement />} />
          <Route path="reports" element={<div className="p-6"><h1 className="text-3xl font-bold">Reports (Coming Soon)</h1></div>} />
          <Route path="settings" element={<div className="p-6"><h1 className="text-3xl font-bold">Settings (Coming Soon)</h1></div>} />
        </Routes>
      </div>
    </div>
  );
};

export default AdminDashboardPage;