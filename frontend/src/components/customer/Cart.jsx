import React from 'react';
import { useCart } from '../../context/CartContext';
import { FaTrash, FaMinus, FaPlus } from 'react-icons/fa';
import { Link } from 'react-router-dom';

const Cart = () => {
  const { cartItems, removeFromCart, updateQuantity, getCartTotal } = useCart();

  if (cartItems.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-600 text-xl mb-4">Your cart is empty</p>
        <Link
          to="/products"
          className="bg-primary text-white px-6 py-3 rounded-lg hover:bg-blue-600"
        >
          Continue Shopping
        </Link>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold mb-6">Shopping Cart</h2>

      {/* Cart Items */}
      <div className="space-y-4">
        {cartItems.map((item) => (
          <div
            key={item.id}
            className="flex items-center space-x-4 border-b pb-4"
          >
            {/* Product Image */}
            <img
              src={item.imageUrl || 'https://via.placeholder.com/100'}
              alt={item.name}
              className="w-24 h-24 object-cover rounded"
            />

            {/* Product Info */}
            <div className="flex-1">
              <h3 className="font-semibold text-lg">{item.name}</h3>
              <p className="text-gray-600">${item.price}</p>
            </div>

            {/* Quantity Controls */}
            <div className="flex items-center space-x-2">
              <button
                onClick={() => updateQuantity(item.id, item.quantity - 1)}
                className="bg-gray-200 p-2 rounded hover:bg-gray-300"
              >
                <FaMinus />
              </button>
              <span className="w-12 text-center font-semibold">
                {item.quantity}
              </span>
              <button
                onClick={() => updateQuantity(item.id, item.quantity + 1)}
                className="bg-gray-200 p-2 rounded hover:bg-gray-300"
              >
                <FaPlus />
              </button>
            </div>

            {/* Subtotal */}
            <div className="text-right">
              <p className="font-bold text-lg">
                ${(item.price * item.quantity).toFixed(2)}
              </p>
            </div>

            {/* Remove Button */}
            <button
              onClick={() => removeFromCart(item.id)}
              className="text-red-500 hover:text-red-700"
            >
              <FaTrash />
            </button>
          </div>
        ))}
      </div>

      {/* Cart Summary */}
      <div className="mt-6 border-t pt-6">
        <div className="flex justify-between items-center mb-4">
          <span className="text-xl font-semibold">Total:</span>
          <span className="text-2xl font-bold text-primary">
            ${getCartTotal().toFixed(2)}
          </span>
        </div>

        <Link
          to="/checkout"
          className="block w-full bg-primary text-white text-center py-3 rounded-lg font-semibold hover:bg-blue-600"
        >
          Proceed to Checkout
        </Link>
      </div>
    </div>
  );
};

export default Cart;