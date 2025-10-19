import React from 'react';
import Checkout from '../../components/customer/Checkout';
import { useCart } from '../../context/CartContext';
import { useAuth } from '../../context/AuthContext';
import { Navigate } from 'react-router-dom';

const CheckoutPage = () => {
  const { cartItems } = useCart();
  const { isAuthenticated } = useAuth();

  // Redirect to login if not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: '/checkout' }} />;
  }

  // Redirect to cart if cart is empty
  if (cartItems.length === 0) {
    return <Navigate to="/cart" />;
  }

  return (
    <div>
      <Checkout />
    </div>
  );
};

export default CheckoutPage;