import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import productService from '../../services/productService';
import categoryService from '../../services/categoryService';
import ProductList from '../../components/customer/ProductList';
import { FaFilter, FaTimes } from 'react-icons/fa';

const ProductsPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Filters
  const [selectedCategory, setSelectedCategory] = useState(searchParams.get('category') || '');
  const [searchKeyword, setSearchKeyword] = useState(searchParams.get('search') || '');
  const [priceRange, setPriceRange] = useState({
    min: searchParams.get('minPrice') || 0,
    max: searchParams.get('maxPrice') || 10000,
  });
  const [selectedBrand, setSelectedBrand] = useState(searchParams.get('brand') || '');
  const [sortBy, setSortBy] = useState(searchParams.get('sort') || '');
  
  // Mobile filter toggle
  const [showFilters, setShowFilters] = useState(false);

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [selectedCategory, searchKeyword, priceRange, selectedBrand, sortBy]);

  const fetchCategories = async () => {
    try {
      const response = await categoryService.getActiveRootCategories();
      setCategories(response.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError(null);

      let response;

      // Fetch based on filters
      if (searchKeyword) {
        response = await productService.searchProducts(searchKeyword);
      } else if (selectedCategory) {
        response = await productService.getProductsByCategory(selectedCategory);
      } else if (selectedBrand) {
        response = await productService.getProductsByBrand(selectedBrand);
      } else {
        response = await productService.getAllProducts();
      }

      let filteredProducts = response.data;

      // Apply price range filter
      if (priceRange.min > 0 || priceRange.max < 10000) {
        filteredProducts = filteredProducts.filter(
          (p) => p.price >= priceRange.min && p.price <= priceRange.max
        );
      }

      // Apply sorting
      if (sortBy === 'price-low') {
        filteredProducts.sort((a, b) => a.price - b.price);
      } else if (sortBy === 'price-high') {
        filteredProducts.sort((a, b) => b.price - a.price);
      } else if (sortBy === 'name') {
        filteredProducts.sort((a, b) => a.name.localeCompare(b.name));
      } else if (sortBy === 'newest') {
        filteredProducts.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      }

      setProducts(filteredProducts);
    } catch (err) {
      console.error('Error fetching products:', err);
      setError('Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  const handleCategoryChange = (categoryId) => {
    setSelectedCategory(categoryId);
    setSearchKeyword(''); // Clear search when category is selected
    updateUrlParams({ category: categoryId, search: '' });
  };

  const handleSearchChange = (keyword) => {
    setSearchKeyword(keyword);
    setSelectedCategory(''); // Clear category when searching
    updateUrlParams({ search: keyword, category: '' });
  };

  const handlePriceRangeChange = (min, max) => {
    setPriceRange({ min, max });
    updateUrlParams({ minPrice: min, maxPrice: max });
  };

  const handleSortChange = (sort) => {
    setSortBy(sort);
    updateUrlParams({ sort });
  };

  const updateUrlParams = (params) => {
    const newParams = new URLSearchParams(searchParams);
    Object.keys(params).forEach((key) => {
      if (params[key]) {
        newParams.set(key, params[key]);
      } else {
        newParams.delete(key);
      }
    });
    setSearchParams(newParams);
  };

  const clearFilters = () => {
    setSelectedCategory('');
    setSearchKeyword('');
    setPriceRange({ min: 0, max: 10000 });
    setSelectedBrand('');
    setSortBy('');
    setSearchParams({});
  };

  const hasActiveFilters = selectedCategory || searchKeyword || priceRange.min > 0 || priceRange.max < 10000 || selectedBrand;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Page Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2">Products</h1>
        <p className="text-gray-600">
          Showing {products.length} {products.length === 1 ? 'product' : 'products'}
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
        {/* Sidebar Filters */}
        <aside className={`
          lg:block lg:col-span-1
          ${showFilters ? 'fixed inset-0 z-50 bg-white p-4 overflow-y-auto' : 'hidden'}
        `}>
          {/* Mobile Filter Header */}
          <div className="lg:hidden flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold">Filters</h2>
            <button onClick={() => setShowFilters(false)} className="text-2xl">
              <FaTimes />
            </button>
          </div>

          <div className="space-y-6">
            {/* Clear Filters */}
            {hasActiveFilters && (
              <button
                onClick={clearFilters}
                className="w-full bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
              >
                Clear All Filters
              </button>
            )}

            {/* Search */}
            <div>
              <h3 className="font-semibold mb-3">Search</h3>
              <input
                type="text"
                placeholder="Search products..."
                value={searchKeyword}
                onChange={(e) => handleSearchChange(e.target.value)}
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            {/* Categories */}
            <div>
              <h3 className="font-semibold mb-3">Categories</h3>
              <div className="space-y-2">
                <label className="flex items-center cursor-pointer">
                  <input
                    type="radio"
                    name="category"
                    checked={selectedCategory === ''}
                    onChange={() => handleCategoryChange('')}
                    className="mr-2"
                  />
                  <span>All Categories</span>
                </label>
                {categories.map((category) => (
                  <label key={category.id} className="flex items-center cursor-pointer">
                    <input
                      type="radio"
                      name="category"
                      checked={selectedCategory === category.id.toString()}
                      onChange={() => handleCategoryChange(category.id.toString())}
                      className="mr-2"
                    />
                    <span>{category.name}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Price Range */}
            <div>
              <h3 className="font-semibold mb-3">Price Range</h3>
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">${priceRange.min}</span>
                  <span className="text-sm text-gray-600">${priceRange.max}</span>
                </div>
                <input
                  type="range"
                  min="0"
                  max="10000"
                  step="50"
                  value={priceRange.max}
                  onChange={(e) => handlePriceRangeChange(priceRange.min, parseInt(e.target.value))}
                  className="w-full"
                />
                <div className="flex space-x-2">
                  <input
                    type="number"
                    placeholder="Min"
                    value={priceRange.min}
                    onChange={(e) => handlePriceRangeChange(parseInt(e.target.value) || 0, priceRange.max)}
                    className="w-1/2 px-2 py-1 border rounded"
                  />
                  <input
                    type="number"
                    placeholder="Max"
                    value={priceRange.max}
                    onChange={(e) => handlePriceRangeChange(priceRange.min, parseInt(e.target.value) || 10000)}
                    className="w-1/2 px-2 py-1 border rounded"
                  />
                </div>
              </div>
            </div>

            {/* Brand Filter (if you have brands) */}
            <div>
              <h3 className="font-semibold mb-3">Brand</h3>
              <input
                type="text"
                placeholder="Enter brand name..."
                value={selectedBrand}
                onChange={(e) => setSelectedBrand(e.target.value)}
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>
          </div>
        </aside>

        {/* Main Content */}
        <main className="lg:col-span-3">
          {/* Toolbar */}
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 space-y-4 sm:space-y-0">
            {/* Mobile Filter Button */}
            <button
              onClick={() => setShowFilters(true)}
              className="lg:hidden flex items-center space-x-2 bg-primary text-white px-4 py-2 rounded-lg"
            >
              <FaFilter />
              <span>Filters</span>
            </button>

            {/* Sort Dropdown */}
            <div className="flex items-center space-x-2">
              <label className="text-gray-600">Sort by:</label>
              <select
                value={sortBy}
                onChange={(e) => handleSortChange(e.target.value)}
                className="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              >
                <option value="">Default</option>
                <option value="name">Name (A-Z)</option>
                <option value="price-low">Price (Low to High)</option>
                <option value="price-high">Price (High to Low)</option>
                <option value="newest">Newest First</option>
              </select>
            </div>
          </div>

          {/* Active Filters Display */}
          {hasActiveFilters && (
            <div className="mb-6 flex flex-wrap gap-2">
              {selectedCategory && (
                <span className="bg-primary text-white px-3 py-1 rounded-full text-sm flex items-center">
                  Category: {categories.find(c => c.id.toString() === selectedCategory)?.name}
                  <button onClick={() => handleCategoryChange('')} className="ml-2">
                    <FaTimes />
                  </button>
                </span>
              )}
              {searchKeyword && (
                <span className="bg-primary text-white px-3 py-1 rounded-full text-sm flex items-center">
                  Search: {searchKeyword}
                  <button onClick={() => handleSearchChange('')} className="ml-2">
                    <FaTimes />
                  </button>
                </span>
              )}
              {(priceRange.min > 0 || priceRange.max < 10000) && (
                <span className="bg-primary text-white px-3 py-1 rounded-full text-sm flex items-center">
                  Price: ${priceRange.min} - ${priceRange.max}
                  <button onClick={() => handlePriceRangeChange(0, 10000)} className="ml-2">
                    <FaTimes />
                  </button>
                </span>
              )}
              {selectedBrand && (
                <span className="bg-primary text-white px-3 py-1 rounded-full text-sm flex items-center">
                  Brand: {selectedBrand}
                  <button onClick={() => setSelectedBrand('')} className="ml-2">
                    <FaTimes />
                  </button>
                </span>
              )}
            </div>
          )}

          {/* Product Grid */}
          <ProductList products={products} loading={loading} error={error} />
        </main>
      </div>
    </div>
  );
};

export default ProductsPage;