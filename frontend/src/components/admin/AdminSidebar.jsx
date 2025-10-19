import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  FaTachometerAlt, 
  FaBox, 
  FaShoppingCart, 
  FaWarehouse, 
  FaUsers, 
  FaTags,
  FaChartLine,
  FaCog
} from 'react-icons/fa';

const AdminSidebar = () => {
  const location = useLocation();

  const menuItems = [
    { path: '/admin', icon: FaTachometerAlt, label: 'Dashboard', exact: true },
    { path: '/admin/products', icon: FaBox, label: 'Products' },
    { path: '/admin/categories', icon: FaTags, label: 'Categories' },
    { path: '/admin/orders', icon: FaShoppingCart, label: 'Orders' },
    { path: '/admin/inventory', icon: FaWarehouse, label: 'Inventory' },
    { path: '/admin/users', icon: FaUsers, label: 'Users' },
    { path: '/admin/reports', icon: FaChartLine, label: 'Reports' },
    { path: '/admin/settings', icon: FaCog, label: 'Settings' },
  ];

  const isActive = (path, exact = false) => {
    if (exact) {
      return location.pathname === path;
    }
    return location.pathname.startsWith(path);
  };

  return (
    <aside className="bg-gray-800 text-white w-64 min-h-screen p-4">
      {/* Logo */}
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-center py-4">Admin Panel</h2>
      </div>

      {/* Menu Items */}
      <nav>
        <ul className="space-y-2">
          {menuItems.map((item) => {
            const Icon = item.icon;
            const active = isActive(item.path, item.exact);

            return (
              <li key={item.path}>
                <Link
                  to={item.path}
                  className={`
                    flex items-center space-x-3 px-4 py-3 rounded-lg transition
                    ${active
                      ? 'bg-primary text-white'
                      : 'text-gray-300 hover:bg-gray-700 hover:text-white'
                    }
                  `}
                >
                  <Icon className="text-xl" />
                  <span className="font-medium">{item.label}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>

      {/* Back to Store */}
      <div className="mt-8 pt-8 border-t border-gray-700">
        <Link
          to="/"
          className="flex items-center justify-center space-x-2 px-4 py-3 bg-green-600 rounded-lg hover:bg-green-700 transition"
        >
          <span>← Back to Store</span>
        </Link>
      </div>
    </aside>
  );
};

export default AdminSidebar;