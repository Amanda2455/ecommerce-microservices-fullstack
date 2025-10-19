import React from 'react';
import { Link } from 'react-router-dom';
import Cart from '../../components/customer/Cart';
import { useCart } from '../../context/CartContext';
import { FaShoppingBag, FaLock } from 'react-icons/fa';

const CartPage = () => {
  const { cartItems, getCartTotal } = useCart();

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Page Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2 flex items-center">
          <FaShoppingBag className="mr-3 text-primary" />
          Shopping Cart
        </h1>
        <p className="text-gray-600">
          {cartItems.length} {cartItems.length === 1 ? 'item' : 'items'} in your cart
        </p>
      </div>

      {cartItems.length === 0 ? (
        // Empty Cart
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <div className="text-gray-400 mb-6">
            <FaShoppingBag className="text-8xl mx-auto" />
          </div>
          <h2 className="text-2xl font-bold mb-4">Your Cart is Empty</h2>
          <p className="text-gray-600 mb-8">
            Looks like you haven't added anything to your cart yet.
          </p>
          <Link
            to="/products"
            className="inline-block bg-primary text-white px-8 py-3 rounded-lg font-semibold hover:bg-blue-600"
          >
            Start Shopping
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Cart Items - Left Side */}
          <div className="lg:col-span-2">
            <Cart />
            
            {/* Continue Shopping Link */}
            <div className="mt-6">
              <Link
                to="/products"
                className="text-primary hover:underline flex items-center"
              >
                ← Continue Shopping
              </Link>
            </div>
          </div>

          {/* Order Summary - Right Side */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-md p-6 sticky top-4">
              <h2 className="text-2xl font-bold mb-6">Order Summary</h2>

              {/* Price Breakdown */}
              <div className="space-y-3 mb-6">
                <div className="flex justify-between text-gray-600">
                  <span>Subtotal ({cartItems.length} items):</span>
                  <span className="font-semibold">${getCartTotal().toFixed(2)}</span>
                </div>

                <div className="flex justify-between text-gray-600">
                  <span>Estimated Tax (10%):</span>
                  <span className="font-semibold">${(getCartTotal() * 0.1).toFixed(2)}</span>
                </div>

                <div className="flex justify-between text-gray-600">
                  <span>Shipping:</span>
                  <span className="font-semibold">
                    {getCartTotal() > 50 ? (
                      <span className="text-green-600">FREE</span>
                    ) : (
                      '$10.00'
                    )}
                  </span>
                </div>

                {getCartTotal() <= 50 && (
                  <div className="bg-blue-50 p-3 rounded text-sm text-blue-700">
                    Add ${(50 - getCartTotal()).toFixed(2)} more to get FREE shipping!
                  </div>
                )}

                <div className="border-t pt-3 mt-3">
                  <div className="flex justify-between text-xl font-bold">
                    <span>Total:</span>
                    <span className="text-primary">
                      ${(
                        getCartTotal() + 
                        getCartTotal() * 0.1 + 
                        (getCartTotal() > 50 ? 0 : 10)
                      ).toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              {/* Checkout Button */}
              <Link
                to="/checkout"
                className="block w-full bg-primary text-white text-center py-3 rounded-lg font-semibold hover:bg-blue-600 mb-4"
              >
                Proceed to Checkout
              </Link>

              {/* Security Badge */}
              <div className="text-center text-sm text-gray-600">
                <FaLock className="inline mr-2" />
                Secure Checkout
              </div>

              {/* Payment Methods */}
              <div className="mt-6 pt-6 border-t">
                <p className="text-sm text-gray-600 mb-3 text-center">We Accept:</p>
                <div className="flex justify-center space-x-3">
                  <div className="bg-gray-100 px-3 py-2 rounded text-xs font-semibold">VISA</div>
                  <div className="bg-gray-100 px-3 py-2 rounded text-xs font-semibold">Mastercard</div>
                  <div className="bg-gray-100 px-3 py-2 rounded text-xs font-semibold">PayPal</div>
                  <div className="bg-gray-100 px-3 py-2 rounded text-xs font-semibold">COD</div>
                </div>
              </div>
            </div>

            {/* Promo Code (Optional) */}
            <div className="bg-white rounded-lg shadow-md p-6 mt-6">
              <h3 className="font-semibold mb-3">Have a Promo Code?</h3>
              <div className="flex space-x-2">
                <input
                  type="text"
                  placeholder="Enter code"
                  className="flex-1 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                />
                <button className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300">
                  Apply
                </button>
              </div>
            </div>

            {/* Delivery Info */}
            <div className="bg-blue-50 rounded-lg p-6 mt-6">
              <h3 className="font-semibold mb-3">Delivery Information</h3>
              <ul className="text-sm text-gray-700 space-y-2">
                <li className="flex items-start">
                  <span className="text-green-500 mr-2">✓</span>
                  <span>Free shipping on orders over $50</span>
                </li>
                <li className="flex items-start">
                  <span className="text-green-500 mr-2">✓</span>
                  <span>Estimated delivery: 3-5 business days</span>
                </li>
                <li className="flex items-start">
                  <span className="text-green-500 mr-2">✓</span>
                  <span>30-day return policy</span>
                </li>
                <li className="flex items-start">
                  <span className="text-green-500 mr-2">✓</span>
                  <span>Cash on Delivery available</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      )}

      {/* Trust Badges */}
      {cartItems.length > 0 && (
        <div className="mt-12 bg-gray-50 rounded-lg p-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-center">
            <div>
              <div className="text-4xl mb-3">🔒</div>
              <h3 className="font-semibold mb-2">Secure Payment</h3>
              <p className="text-sm text-gray-600">
                Your payment information is processed securely
              </p>
            </div>
            <div>
              <div className="text-4xl mb-3">🚚</div>
              <h3 className="font-semibold mb-2">Fast Delivery</h3>
              <p className="text-sm text-gray-600">
                Quick and reliable shipping to your doorstep
              </p>
            </div>
            <div>
              <div className="text-4xl mb-3">↩️</div>
              <h3 className="font-semibold mb-2">Easy Returns</h3>
              <p className="text-sm text-gray-600">
                30-day hassle-free return policy
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartPage;