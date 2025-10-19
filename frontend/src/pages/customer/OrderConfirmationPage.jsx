import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import orderService from '../../services/orderService';
import Loading from '../../components/common/Loading';
import { FaCheckCircle, FaBox, FaFileInvoice } from 'react-icons/fa';

const OrderConfirmationPage = () => {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrderDetails();
  }, [orderId]);

  const fetchOrderDetails = async () => {
    try {
      setLoading(true);
      const response = await orderService.getOrderById(orderId);
      setOrder(response.data);
    } catch (error) {
      console.error('Error fetching order:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Loading />;
  }

  if (!order) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-8 text-center">
        <h2 className="text-2xl font-bold mb-4">Order Not Found</h2>
        <Link to="/orders" className="text-primary hover:underline">
          View Your Orders
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      {/* Success Message */}
      <div className="bg-green-50 border-2 border-green-500 rounded-lg p-8 mb-8 text-center">
        <FaCheckCircle className="text-6xl text-green-500 mx-auto mb-4" />
        <h1 className="text-3xl font-bold text-green-800 mb-2">
          Order Placed Successfully!
        </h1>
        <p className="text-green-700 mb-4">
          Thank you for your order. We've received your order and will process it shortly.
        </p>
        <div className="bg-white inline-block px-6 py-3 rounded-lg">
          <p className="text-sm text-gray-600">Order Number</p>
          <p className="text-2xl font-bold text-primary">{order.orderNumber}</p>
        </div>
      </div>

      {/* Order Details */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 className="text-2xl font-bold mb-6">Order Details</h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          {/* Order Info */}
          <div>
            <h3 className="font-semibold mb-3 text-gray-700">Order Information</h3>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-600">Order Date:</span>
                <span className="font-semibold">
                  {new Date(order.createdAt).toLocaleDateString()}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Order Status:</span>
                <span className="font-semibold text-primary">{order.status}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Payment Method:</span>
                <span className="font-semibold">{order.paymentMethod || 'N/A'}</span>
              </div>
            </div>
          </div>

          {/* Shipping Address */}
          <div>
            <h3 className="font-semibold mb-3 text-gray-700">Shipping Address</h3>
            <div className="text-sm text-gray-700">
              <p className="font-semibold">
                {order.shippingAddress?.firstName} {order.shippingAddress?.lastName}
              </p>
              <p>{order.shippingAddress?.addressLine1}</p>
              <p>
                {order.shippingAddress?.city}, {order.shippingAddress?.state}{' '}
                {order.shippingAddress?.zipCode}
              </p>
              <p>{order.shippingAddress?.country}</p>
              <p className="mt-2">Phone: {order.shippingAddress?.phoneNumber}</p>
            </div>
          </div>
        </div>

        {/* Order Items */}
        <div className="border-t pt-6">
          <h3 className="font-semibold mb-4 text-gray-700">Order Items</h3>
          <div className="space-y-4">
            {order.items?.map((item) => (
              <div
                key={item.id}
                className="flex items-center space-x-4 pb-4 border-b last:border-b-0"
              >
                <img
                  src={item.product?.imageUrl || 'https://via.placeholder.com/80'}
                  alt={item.product?.name}
                  className="w-20 h-20 object-cover rounded"
                />
                <div className="flex-1">
                  <h4 className="font-semibold">{item.product?.name}</h4>
                  <p className="text-sm text-gray-600">Quantity: {item.quantity}</p>
                </div>
                <div className="text-right">
                  <p className="font-semibold">${item.price?.toFixed(2)}</p>
                  <p className="text-sm text-gray-600">each</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Order Summary */}
        <div className="border-t pt-6 mt-6">
          <div className="space-y-2 max-w-sm ml-auto">
            <div className="flex justify-between text-gray-600">
              <span>Subtotal:</span>
              <span className="font-semibold">${order.subtotal?.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-gray-600">
              <span>Tax:</span>
              <span className="font-semibold">${order.tax?.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-gray-600">
              <span>Shipping:</span>
              <span className="font-semibold">${order.shippingCost?.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-xl font-bold border-t pt-2">
              <span>Total:</span>
              <span className="text-primary">${order.totalAmount?.toFixed(2)}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <Link
          to={`/orders/${order.id}`}
          className="bg-primary text-white text-center py-3 rounded-lg font-semibold hover:bg-blue-600 flex items-center justify-center"
        >
          <FaBox className="mr-2" />
          Track Order
        </Link>
        <Link
          to="/orders"
          className="bg-gray-200 text-gray-700 text-center py-3 rounded-lg font-semibold hover:bg-gray-300 flex items-center justify-center"
        >
          <FaFileInvoice className="mr-2" />
          View All Orders
        </Link>
        <Link
          to="/products"
          className="bg-white border-2 border-primary text-primary text-center py-3 rounded-lg font-semibold hover:bg-gray-50 flex items-center justify-center"
        >
          Continue Shopping
        </Link>
      </div>

      {/* What's Next */}
      <div className="bg-blue-50 rounded-lg p-6">
        <h3 className="font-bold text-lg mb-4">What Happens Next?</h3>
        <div className="space-y-3">
          <div className="flex items-start">
            <div className="bg-primary text-white rounded-full w-8 h-8 flex items-center justify-center mr-3 flex-shrink-0">
              1
            </div>
            <div>
              <h4 className="font-semibold">Order Confirmation</h4>
              <p className="text-sm text-gray-600">
                You'll receive an email confirmation shortly with your order details.
              </p>
            </div>
          </div>
          <div className="flex items-start">
            <div className="bg-primary text-white rounded-full w-8 h-8 flex items-center justify-center mr-3 flex-shrink-0">
              2
            </div>
            <div>
              <h4 className="font-semibold">Order Processing</h4>
              <p className="text-sm text-gray-600">
                We'll process your order and prepare it for shipping.
              </p>
            </div>
          </div>
          <div className="flex items-start">
            <div className="bg-primary text-white rounded-full w-8 h-8 flex items-center justify-center mr-3 flex-shrink-0">
              3
            </div>
            <div>
              <h4 className="font-semibold">Shipping</h4>
              <p className="text-sm text-gray-600">
                Your order will be shipped and you'll receive tracking information.
              </p>
            </div>
          </div>
          <div className="flex items-start">
            <div className="bg-primary text-white rounded-full w-8 h-8 flex items-center justify-center mr-3 flex-shrink-0">
              4
            </div>
            <div>
              <h4 className="font-semibold">Delivery</h4>
              <p className="text-sm text-gray-600">
                Your order will be delivered within 3-5 business days.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Need Help */}
      <div className="text-center mt-8 p-6 bg-gray-50 rounded-lg">
        <h3 className="font-semibold mb-2">Need Help?</h3>
        <p className="text-gray-600 mb-4">
          If you have any questions about your order, please contact our support team.
        </p>
        <Link
          to="/contact"
          className="text-primary hover:underline font-semibold"
        >
          Contact Support →
        </Link>
      </div>
    </div>
  );
};

export default OrderConfirmationPage;