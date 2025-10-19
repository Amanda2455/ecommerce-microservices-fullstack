import React, { useState, useEffect } from 'react';
import inventoryService from '../../services/inventoryService';
import productService from '../../services/productService';
import { toast } from 'react-toastify';
import { FaPlus, FaMinus, FaExclamationTriangle, FaSearch } from 'react-icons/fa';
import Loading from '../common/Loading';

const InventoryManagement = () => {
  const [inventory, setInventory] = useState([]);
  const [filteredInventory, setFilteredInventory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filter, setFilter] = useState('ALL');
  const [showModal, setShowModal] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [adjustmentData, setAdjustmentData] = useState({
    quantity: '',
    reason: '',
    reference: '',
  });

  useEffect(() => {
    fetchInventory();
  }, []);

  useEffect(() => {
    filterInventory();
  }, [filter, searchTerm, inventory]);

  const fetchInventory = async () => {
    try {
      setLoading(true);
      let response;
      
      if (filter === 'LOW_STOCK') {
        response = await inventoryService.getLowStockItems();
      } else if (filter === 'OUT_OF_STOCK') {
        response = await inventoryService.getOutOfStockItems();
      } else {
        response = await inventoryService.getAllInventory();
      }
      
      setInventory(response.data);
    } catch (error) {
      console.error('Error fetching inventory:', error);
      toast.error('Failed to load inventory');
    } finally {
      setLoading(false);
    }
  };

  const filterInventory = () => {
    let filtered = inventory;

    // Search filter
    if (searchTerm) {
      filtered = filtered.filter(item =>
        item.productName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        item.sku?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredInventory(filtered);
  };

  const handleAdjustStock = async (type) => {
    if (!adjustmentData.quantity || !selectedItem) return;

    try {
      const data = {
        quantity: parseInt(adjustmentData.quantity),
        reason: adjustmentData.reason,
        reference: adjustmentData.reference,
      };

      if (type === 'ADD') {
        await inventoryService.addStock(selectedItem.id, data);
        toast.success('Stock added successfully');
      } else {
        await inventoryService.removeStock(selectedItem.id, data);
        toast.success('Stock removed successfully');
      }

      setShowModal(false);
      setSelectedItem(null);
      setAdjustmentData({ quantity: '', reason: '', reference: '' });
      fetchInventory();
    } catch (error) {
      console.error('Error adjusting stock:', error);
      toast.error('Failed to adjust stock');
    }
  };

  const openAdjustmentModal = (item) => {
    setSelectedItem(item);
    setShowModal(true);
  };

  const getStockStatus = (item) => {
    if (item.availableQuantity === 0) {
      return { label: 'Out of Stock', color: 'text-red-600' };
    } else if (item.availableQuantity <= item.reorderLevel) {
      return { label: 'Low Stock', color: 'text-yellow-600' };
    } else {
      return { label: 'In Stock', color: 'text-green-600' };
    }
  };

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold">Inventory Management</h1>
        <p className="text-gray-600">Monitor and manage stock levels</p>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* Search */}
          <div className="relative">
            <FaSearch className="absolute left-3 top-3 text-gray-400" />
            <input
              type="text"
              placeholder="Search by product name or SKU..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>

          {/* Filter */}
          <div>
            <select
              value={filter}
              onChange={(e) => {
                setFilter(e.target.value);
                fetchInventory();
              }}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            >
              <option value="ALL">All Items</option>
              <option value="LOW_STOCK">Low Stock</option>
              <option value="OUT_OF_STOCK">Out of Stock</option>
            </select>
          </div>

          {/* Stats */}
          <div className="flex items-center justify-end space-x-4">
            <div className="text-center">
              <p className="text-sm text-gray-600">Total Items</p>
              <p className="text-2xl font-bold">{inventory.length}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Inventory Table */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="text-left py-3 px-4">Product</th>
                <th className="text-left py-3 px-4">SKU</th>
                <th className="text-left py-3 px-4">Available</th>
                <th className="text-left py-3 px-4">Reserved</th>
                <th className="text-left py-3 px-4">Reorder Level</th>
                <th className="text-left py-3 px-4">Status</th>
                <th className="text-center py-3 px-4">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredInventory.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-gray-500">
                    No inventory items found
                  </td>
                </tr>
              ) : (
                filteredInventory.map((item) => {
                  const status = getStockStatus(item);
                  return (
                    <tr key={item.id} className="border-b hover:bg-gray-50">
                      <td className="py-3 px-4 font-semibold">
                        {item.productName || `Product #${item.productId}`}
                      </td>
                      <td className="py-3 px-4 font-mono text-sm">{item.sku}</td>
                      <td className="py-3 px-4">
                        <span className="font-bold">{item.availableQuantity}</span>
                      </td>
                      <td className="py-3 px-4 text-gray-600">
                        {item.reservedQuantity || 0}
                      </td>
                      <td className="py-3 px-4 text-gray-600">
                        {item.reorderLevel || 10}
                      </td>
                      <td className="py-3 px-4">
                        <span className={`font-semibold ${status.color} flex items-center`}>
                          {item.availableQuantity <= (item.reorderLevel || 10) && (
                            <FaExclamationTriangle className="mr-1" />
                          )}
                          {status.label}
                        </span>
                      </td>
                      <td className="py-3 px-4">
                        <div className="flex justify-center space-x-2">
                          <button
                            onClick={() => openAdjustmentModal(item)}
                            className="bg-primary text-white px-3 py-1 rounded hover:bg-blue-600 text-sm"
                          >
                            Adjust Stock
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Stock Adjustment Modal */}
      {showModal && selectedItem && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-8 max-w-md w-full">
            <h2 className="text-2xl font-bold mb-6">Adjust Stock</h2>
            
            <div className="space-y-4">
              <div>
                <p className="text-sm text-gray-600 mb-1">Product</p>
                <p className="font-semibold">{selectedItem.productName}</p>
              </div>

              <div>
                <p className="text-sm text-gray-600 mb-1">Current Stock</p>
                <p className="font-semibold text-lg">{selectedItem.availableQuantity} units</p>
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">Quantity</label>
                <input
                  type="number"
                  value={adjustmentData.quantity}
                  onChange={(e) => setAdjustmentData({...adjustmentData, quantity: e.target.value})}
                  min="1"
                  placeholder="Enter quantity"
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">Reason</label>
                <select
                  value={adjustmentData.reason}
                  onChange={(e) => setAdjustmentData({...adjustmentData, reason: e.target.value})}
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="">Select reason</option>
                  <option value="RESTOCK">Restock</option>
                  <option value="DAMAGE">Damage</option>
                  <option value="RETURN">Return</option>
                  <option value="CORRECTION">Correction</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium mb-2">Reference (Optional)</label>
                <input
                  type="text"
                  value={adjustmentData.reference}
                  onChange={(e) => setAdjustmentData({...adjustmentData, reference: e.target.value})}
                  placeholder="PO number, invoice, etc."
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div className="flex space-x-4 pt-4">
                <button
                  onClick={() => handleAdjustStock('ADD')}
                  className="flex-1 bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 flex items-center justify-center"
                >
                  <FaPlus className="mr-2" />
                  Add Stock
                </button>
                <button
                  onClick={() => handleAdjustStock('REMOVE')}
                  className="flex-1 bg-red-500 text-white py-2 rounded-lg hover:bg-red-600 flex items-center justify-center"
                >
                  <FaMinus className="mr-2" />
                  Remove Stock
                </button>
              </div>

              <button
                onClick={() => {
                  setShowModal(false);
                  setSelectedItem(null);
                  setAdjustmentData({ quantity: '', reason: '', reference: '' });
                }}
                className="w-full mt-2 px-6 py-2 bg-gray-200 rounded-lg hover:bg-gray-300"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default InventoryManagement;