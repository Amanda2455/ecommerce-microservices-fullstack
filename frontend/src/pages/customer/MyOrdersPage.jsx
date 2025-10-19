import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import orderService from '../../services/orderService';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import { FaBox, FaEye, FaTimes } from 'react-icons/fa';

const MyOrdersPage = () => {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [filteredOrders, setFilteredOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [statusFilter, setStatusFilter] = useState('ALL');

  useEffect(() => {
    if (user) {
      fetchOrders();
    }
  }, [user]);

  useEffect(() => {
    filterOrders();
  }, [statusFilter, orders]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const response = await orderService.getOrdersByUserId(user.id);
      setOrders(response.data);
      setFilteredOrders(response.data);
      setError(null);
    } catch (err) {
      console.error('Error fetching orders:', err);
      setError('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  const filterOrders = () => {
    if (statusFilter === 'ALL') {
      setFilteredOrders(orders);
    } else {
      setFilteredOrders(orders.filter(order => order.status === statusFilter));
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

  const handleCancelOrder = async (orderId) => {
    if (window.confirm('Are you sure you want to cancel this order?')) {
      try {
        await orderService.cancelOrder(orderId, { reason: 'Cancelled by customer' });
        fetchOrders();
        alert('Order cancelled successfully');
      } catch (error) {
        alert('Failed to cancel order');
      }
    }
  };

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8">
        <ErrorMessage message={error} />
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Page Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2 flex items-center">
          <FaBox className="mr-3 text-primary" />
          My Orders
        </h1>
        <p className="text-gray-600">
          Track and manage your orders
        </p>
      </div>

      {/* Status Filter */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <div className="flex flex-wrap gap-2">
          {['ALL', 'PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'].map((status) => (
            <button
              key={status}
              onClick={() => setStatusFilter(status)}
              className={`px-4 py-2 rounded-lg font-semibold transition ${
                statusFilter === status
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {status}
            </button>
          ))}
        </div>
      </div>

      {/* Orders List */}
      {filteredOrders.length === 0 ? (
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <FaBox className="text-8xl text-gray-300 mx-auto mb-4" />
          <h2 className="text-2xl font-bold mb-2">No Orders Found</h2>
          <p className="text-gray-600 mb-6">
            {statusFilter === 'ALL' 
              ? "You haven't placed any orders yet."
              : `No orders with status: ${statusFilter}`
            }
          </p>
          <Link
            to="/products"
            className="inline-block bg-primary text-white px-6 py-3 rounded-lg hover:bg-blue-600"
          >
            Start Shopping
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredOrders.map((order) => (
            <div key={order.id} className="bg-white rounded-lg shadow-md overflow-hidden">
              {/* Order Header */}
              <div className="bg-gray-50 px-6 py-4 border-b">
                <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                  <div className="mb-2 md:mb-0">
                    <h3 className="font-semibold text-lg">
                      Order #{order.orderNumber}
                    </h3>
                    <p className="text-sm text-gray-600">
                      Placed on {new Date(order.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                  <div className="flex items-center space-x-3">
                    <span className={`px-3 py-1 rounded-full text-sm font-semibold ${getStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                    <span className="text-lg font-bold text-primary">
                      ${order.totalAmount?.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              {/* Order Items */}
              <div className="px-6 py-4">
                <div className="space-y-3">
                  {order.items?.slice(0, 3).map((item) => (
                    <div key={item.id} className="flex items-center space-x-4">
                      <img
                        src={item.product?.imageUrl || 'https://via.placeholder.com/60'}
                        alt={item.product?.name}
                        className="w-16 h-16 object-cover rounded"
                      />
                      <div className="flex-1">
                        <h4 className="font-semibold text-sm">{item.product?.name}</h4>
                        <p className="text-sm text-gray-600">Qty: {item.quantity}</p>
                      </div>
                      <p className="font-semibold">${(item.price * item.quantity).toFixed(2)}</p>
                    </div>
                  ))}
                  {order.items?.length > 3 && (
                    <p className="text-sm text-gray-600 text-center">
                      + {order.items.length - 3} more items
                    </p>
                  )}
                </div>
              </div>

              {/* Order Actions */}
              <div className="bg-gray-50 px-6 py-4 border-t flex flex-col sm:flex-row gap-3">
                <Link
                  to={`/orders/${order.id}`}
                  className="flex-1 bg-primary text-white text-center py-2 rounded-lg hover:bg-blue-600 flex items-center justify-center"
                >
                  <FaEye className="mr-2" />
                  View Details
                </Link>
                {(order.status === 'PENDING' || order.status === 'CONFIRMED') && (
                  <button
                    onClick={() => handleCancelOrder(order.id)}
                    className="flex-1 bg-red-500 text-white text-center py-2 rounded-lg hover:bg-red-600 flex items-center justify-center"
                  >
                    <FaTimes className="mr-2" />
                    Cancel Order
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyOrdersPage;