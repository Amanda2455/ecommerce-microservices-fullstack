import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import orderService from '../../services/orderService';
import { toast } from 'react-toastify';
import { FaCheckCircle, FaCircle, FaTruck, FaBox, FaClipboardCheck } from 'react-icons/fa';
import Loading from '../common/Loading';
import ErrorMessage from '../common/ErrorMessage';

const OrderTracking = () => {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [statusHistory, setStatusHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchOrderDetails();
  }, [orderId]);

  const fetchOrderDetails = async () => {
    try {
      setLoading(true);
      
      // Fetch order details
      const orderResponse = await orderService.getOrderById(orderId);
      setOrder(orderResponse.data);

      // Fetch status history
      const historyResponse = await orderService.getOrderStatusHistory(orderId);
      setStatusHistory(historyResponse.data);

      setError(null);
    } catch (err) {
      console.error('Error fetching order:', err);
      setError(err.response?.data?.message || 'Failed to load order details');
      toast.error('Failed to load order details');
    } finally {
      setLoading(false);
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'PENDING':
        return <FaCircle className="text-yellow-500" />;
      case 'CONFIRMED':
        return <FaClipboardCheck className="text-blue-500" />;
      case 'PROCESSING':
        return <FaBox className="text-purple-500" />;
      case 'SHIPPED':
        return <FaTruck className="text-orange-500" />;
      case 'DELIVERED':
        return <FaCheckCircle className="text-green-500" />;
      case 'CANCELLED':
        return <FaCircle className="text-red-500" />;
      default:
        return <FaCircle className="text-gray-500" />;
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'CONFIRMED':
        return 'bg-blue-100 text-blue-800';
      case 'PROCESSING':
        return 'bg-purple-100 text-purple-800';
      case 'SHIPPED':
        return 'bg-orange-100 text-orange-800';
      case 'DELIVERED':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return <ErrorMessage message={error} />;
  }

  if (!order) {
    return <div className="text-center py-12">Order not found</div>;
  }

  return (
    <div className="max-w-5xl mx-auto px-4 py-8">
      {/* Order Header */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold mb-2">Order Tracking</h1>
            <p className="text-gray-600">
              Order Number: <span className="font-semibold">{order.orderNumber}</span>
            </p>
            <p className="text-gray-600">
              Order Date: <span className="font-semibold">{formatDate(order.createdAt)}</span>
            </p>
          </div>
          <div>
            <span className={`px-4 py-2 rounded-full font-semibold ${getStatusColor(order.status)}`}>
              {order.status}
            </span>
          </div>
        </div>

        {/* Customer Info */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-6 pt-6 border-t">
          <div>
            <h3 className="font-semibold mb-2">Shipping Address</h3>
            <p className="text-gray-700">{order.shippingAddress?.firstName} {order.shippingAddress?.lastName}</p>
            <p className="text-gray-700">{order.shippingAddress?.addressLine1}</p>
            <p className="text-gray-700">
              {order.shippingAddress?.city}, {order.shippingAddress?.state} {order.shippingAddress?.zipCode}
            </p>
            <p className="text-gray-700">{order.shippingAddress?.country}</p>
            <p className="text-gray-700 mt-2">Phone: {order.shippingAddress?.phoneNumber}</p>
          </div>
          <div>
            <h3 className="font-semibold mb-2">Order Summary</h3>
            <div className="space-y-1 text-gray-700">
              <div className="flex justify-between">
                <span>Subtotal:</span>
                <span>${order.subtotal?.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span>Tax:</span>
                <span>${order.tax?.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span>Shipping:</span>
                <span>${order.shippingCost?.toFixed(2)}</span>
              </div>
              <div className="flex justify-between font-bold text-lg border-t pt-2">
                <span>Total:</span>
                <span className="text-primary">${order.totalAmount?.toFixed(2)}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Order Status Timeline */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 className="text-2xl font-bold mb-6">Order Status Timeline</h2>
        
        <div className="relative">
          {/* Timeline Line */}
          <div className="absolute left-6 top-0 bottom-0 w-0.5 bg-gray-300"></div>

          {/* Status History */}
          <div className="space-y-6">
            {statusHistory.map((history, index) => (
              <div key={history.id} className="relative flex items-start">
                {/* Icon */}
                <div className="relative z-10 flex items-center justify-center w-12 h-12 bg-white rounded-full border-4 border-white">
                  <div className="text-2xl">
                    {getStatusIcon(history.status)}
                  </div>
                </div>

                {/* Content */}
                <div className="ml-6 flex-1">
                  <div className="bg-gray-50 rounded-lg p-4">
                    <div className="flex justify-between items-start mb-2">
                      <h3 className="font-semibold text-lg">{history.status}</h3>
                      <span className="text-sm text-gray-600">
                        {formatDate(history.changedAt)}
                      </span>
                    </div>
                    {history.remarks && (
                      <p className="text-gray-700">{history.remarks}</p>
                    )}
                    {history.changedBy && (
                      <p className="text-sm text-gray-600 mt-2">
                        Updated by: {history.changedBy}
                      </p>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Order Items */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-6">Order Items</h2>
        
        <div className="space-y-4">
          {order.items?.map((item) => (
            <div key={item.id} className="flex items-center space-x-4 pb-4 border-b last:border-b-0">
              <img
                src={item.product?.imageUrl || 'https://via.placeholder.com/100'}
                alt={item.product?.name}
                className="w-20 h-20 object-cover rounded"
              />
              <div className="flex-1">
                <h3 className="font-semibold">{item.product?.name}</h3>
                <p className="text-gray-600 text-sm">SKU: {item.product?.sku}</p>
                <p className="text-gray-600 text-sm">Quantity: {item.quantity}</p>
              </div>
              <div className="text-right">
                <p className="font-semibold text-lg">${item.price?.toFixed(2)}</p>
                <p className="text-sm text-gray-600">each</p>
              </div>
              <div className="text-right">
                <p className="font-bold text-lg text-primary">
                  ${(item.price * item.quantity).toFixed(2)}
                </p>
                <p className="text-sm text-gray-600">subtotal</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Action Buttons */}
      {order.status !== 'CANCELLED' && order.status !== 'DELIVERED' && (
        <div className="mt-6 flex justify-end space-x-4">
          <button className="px-6 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300">
            Contact Support
          </button>
          {(order.status === 'PENDING' || order.status === 'CONFIRMED') && (
            <button 
              onClick={() => {
                if (window.confirm('Are you sure you want to cancel this order?')) {
                  orderService.cancelOrder(order.id, { reason: 'Cancelled by customer' })
                    .then(() => {
                      toast.success('Order cancelled successfully');
                      fetchOrderDetails();
                    })
                    .catch(err => {
                      toast.error('Failed to cancel order');
                    });
                }
              }}
              className="px-6 py-3 bg-red-500 text-white rounded-lg hover:bg-red-600"
            >
              Cancel Order
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default OrderTracking;