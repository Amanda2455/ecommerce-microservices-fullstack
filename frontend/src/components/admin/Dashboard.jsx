import React, { useState, useEffect } from 'react';
import orderService from '../../services/orderService';
import productService from '../../services/productService';
import userService from '../../services/userService';
import inventoryService from '../../services/inventoryService';
import { 
  FaShoppingCart, 
  FaBox, 
  FaUsers, 
  FaDollarSign,
  FaArrowUp,
  FaArrowDown 
} from 'react-icons/fa';
import Loading from '../common/Loading';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalOrders: 0,
    totalProducts: 0,
    totalUsers: 0,
    totalRevenue: 0,
    pendingOrders: 0,
    lowStockItems: 0,
  });
  const [recentOrders, setRecentOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);

      // Fetch all data in parallel
      const [ordersRes, productsRes, usersRes, lowStockRes] = await Promise.all([
        orderService.getAllOrders(),
        productService.getAllProducts(),
        userService.getAllUsers(),
        inventoryService.getLowStockItems(),
      ]);

      const orders = ordersRes.data;
      const products = productsRes.data;
      const users = usersRes.data;

      // Calculate stats
      const totalRevenue = orders
        .filter(o => o.status === 'DELIVERED')
        .reduce((sum, o) => sum + (o.totalAmount || 0), 0);

      const pendingOrders = orders.filter(o => o.status === 'PENDING').length;

      setStats({
        totalOrders: orders.length,
        totalProducts: products.length,
        totalUsers: users.length,
        totalRevenue: totalRevenue,
        pendingOrders: pendingOrders,
        lowStockItems: lowStockRes.data.length,
      });

      // Get recent 5 orders
      setRecentOrders(orders.slice(0, 5));

    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const StatCard = ({ icon: Icon, title, value, color, change }) => (
    <div className="bg-white rounded-lg shadow-md p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-gray-600 text-sm mb-1">{title}</p>
          <p className="text-3xl font-bold">{value}</p>
          {change && (
            <div className={`flex items-center mt-2 text-sm ${change > 0 ? 'text-green-600' : 'text-red-600'}`}>
              {change > 0 ? <FaArrowUp /> : <FaArrowDown />}
              <span className="ml-1">{Math.abs(change)}%</span>
            </div>
          )}
        </div>
        <div className={`${color} p-4 rounded-full`}>
          <Icon className="text-3xl text-white" />
        </div>
      </div>
    </div>
  );

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-8">Dashboard Overview</h1>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard
          icon={FaShoppingCart}
          title="Total Orders"
          value={stats.totalOrders}
          color="bg-blue-500"
          change={12}
        />
        <StatCard
          icon={FaDollarSign}
          title="Total Revenue"
          value={`$${stats.totalRevenue.toFixed(2)}`}
          color="bg-green-500"
          change={8}
        />
        <StatCard
          icon={FaBox}
          title="Total Products"
          value={stats.totalProducts}
          color="bg-purple-500"
          change={5}
        />
        <StatCard
          icon={FaUsers}
          title="Total Users"
          value={stats.totalUsers}
          color="bg-orange-500"
          change={15}
        />
      </div>

      {/* Alerts */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 rounded">
          <div className="flex items-center">
            <FaShoppingCart className="text-yellow-600 text-2xl mr-3" />
            <div>
              <p className="font-semibold text-yellow-800">Pending Orders</p>
              <p className="text-yellow-700">{stats.pendingOrders} orders need attention</p>
            </div>
          </div>
        </div>

        <div className="bg-red-50 border-l-4 border-red-400 p-4 rounded">
          <div className="flex items-center">
            <FaBox className="text-red-600 text-2xl mr-3" />
            <div>
              <p className="font-semibold text-red-800">Low Stock Alert</p>
              <p className="text-red-700">{stats.lowStockItems} products running low</p>
            </div>
          </div>
        </div>
      </div>

      {/* Recent Orders */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4">Recent Orders</h2>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b">
                <th className="text-left py-3 px-4">Order Number</th>
                <th className="text-left py-3 px-4">Customer</th>
                <th className="text-left py-3 px-4">Date</th>
                <th className="text-left py-3 px-4">Status</th>
                <th className="text-right py-3 px-4">Total</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map((order) => (
                <tr key={order.id} className="border-b hover:bg-gray-50">
                  <td className="py-3 px-4 font-semibold">{order.orderNumber}</td>
                  <td className="py-3 px-4">{order.email}</td>
                  <td className="py-3 px-4">
                    {new Date(order.createdAt).toLocaleDateString()}
                  </td>
                  <td className="py-3 px-4">
                    <span className={`px-2 py-1 rounded text-sm ${
                      order.status === 'DELIVERED' ? 'bg-green-100 text-green-800' :
                      order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-blue-100 text-blue-800'
                    }`}>
                      {order.status}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-right font-semibold">
                    ${order.totalAmount?.toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;