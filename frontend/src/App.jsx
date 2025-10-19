import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Layout Components
import Navbar from './components/common/Navbar';
import Footer from './components/common/Footer';

// Customer Pages
import HomePage from './pages/customer/HomePage';
import ProductsPage from './pages/customer/ProductsPage';
import ProductDetailPage from './pages/customer/ProductDetailPage';
import CartPage from './pages/customer/CartPage';
import CheckoutPage from './pages/customer/CheckoutPage';
import OrderConfirmationPage from './pages/customer/OrderConfirmationPage';
import MyOrdersPage from './pages/customer/MyOrdersPage';
import ProfilePage from './pages/customer/ProfilePage';

// Auth Pages
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';

// Admin Pages
import AdminDashboardPage from './pages/admin/AdminDashboardPage';

// Order Tracking
import OrderTracking from './components/customer/OrderTracking';

function App() {
  return (
    <Router>
      <AuthProvider>
        <CartProvider>
          <div className="flex flex-col min-h-screen">
            <Routes>
              {/* Routes WITHOUT Navbar/Footer (Auth & Admin) */}
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/admin/*" element={<AdminDashboardPage />} />

              {/* Routes WITH Navbar/Footer (Customer) */}
              <Route
                path="/*"
                element={
                  <>
                    <Navbar />
                    <main className="flex-1">
                      <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/products" element={<ProductsPage />} />
                        <Route path="/products/:id" element={<ProductDetailPage />} />
                        <Route path="/cart" element={<CartPage />} />
                        <Route path="/checkout" element={<CheckoutPage />} />
                        <Route path="/order-confirmation/:orderId" element={<OrderConfirmationPage />} />
                        <Route path="/orders" element={<MyOrdersPage />} />
                        <Route path="/orders/:orderId" element={<OrderTracking />} />
                        <Route path="/profile" element={<ProfilePage />} />
                        
                        {/* 404 Page */}
                        <Route path="*" element={
                          <div className="flex items-center justify-center min-h-screen">
                            <div className="text-center">
                              <h1 className="text-6xl font-bold text-gray-300 mb-4">404</h1>
                              <p className="text-xl text-gray-600 mb-4">Page Not Found</p>
                              <a href="/" className="text-primary hover:underline">Go back home</a>
                            </div>
                          </div>
                        } />
                      </Routes>
                    </main>
                    <Footer />
                  </>
                }
              />
            </Routes>

            {/* Toast Notifications */}
            <ToastContainer
              position="top-right"
              autoClose={3000}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick
              rtl={false}
              pauseOnFocusLoss
              draggable
              pauseOnHover
            />
          </div>
        </CartProvider>
      </AuthProvider>
    </Router>
  );
}

export default App;