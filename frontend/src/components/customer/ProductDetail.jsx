import React, { useState } from 'react';
import { FaShoppingCart, FaStar, FaMinus, FaPlus, FaHeart } from 'react-icons/fa';
import { useCart } from '../../context/CartContext';
import { toast } from 'react-toastify';

const ProductDetail = ({ product }) => {
  const [quantity, setQuantity] = useState(1);
  const { addToCart } = useCart();

  const handleAddToCart = () => {
    if (product.stockQuantity === 0) {
      toast.error('Product is out of stock!');
      return;
    }

    if (quantity > product.stockQuantity) {
      toast.error(`Only ${product.stockQuantity} items available!`);
      return;
    }

    addToCart(product, quantity);
    toast.success(`${quantity} x ${product.name} added to cart!`);
  };

  const increaseQuantity = () => {
    if (quantity < product.stockQuantity) {
      setQuantity(quantity + 1);
    } else {
      toast.warning(`Maximum available quantity is ${product.stockQuantity}`);
    }
  };

  const decreaseQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-lg overflow-hidden">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 p-8">
        {/* Product Images */}
        <div>
          <div className="relative">
            <img
              src={product.imageUrl || 'https://via.placeholder.com/600'}
              alt={product.name}
              className="w-full h-96 object-cover rounded-lg"
            />
            {product.featured && (
              <span className="absolute top-4 left-4 bg-yellow-400 text-black px-3 py-1 rounded font-semibold">
                FEATURED
              </span>
            )}
          </div>

          {/* Thumbnail Images (if you have multiple images) */}
          <div className="grid grid-cols-4 gap-2 mt-4">
            {[1, 2, 3, 4].map((_, index) => (
              <img
                key={index}
                src={product.imageUrl || 'https://via.placeholder.com/150'}
                alt={`${product.name} ${index + 1}`}
                className="w-full h-20 object-cover rounded cursor-pointer hover:opacity-75"
              />
            ))}
          </div>
        </div>

        {/* Product Information */}
        <div>
          {/* Brand */}
          {product.brand && (
            <p className="text-sm text-gray-500 mb-2">
              Brand: <span className="font-semibold text-primary">{product.brand}</span>
            </p>
          )}

          {/* Product Name */}
          <h1 className="text-3xl font-bold text-gray-800 mb-4">
            {product.name}
          </h1>

          {/* Rating */}
          <div className="flex items-center mb-4">
            <div className="flex">
              {[...Array(5)].map((_, i) => (
                <FaStar
                  key={i}
                  className={`text-lg ${
                    i < (product.rating || 0) ? 'text-yellow-400' : 'text-gray-300'
                  }`}
                />
              ))}
            </div>
            <span className="text-gray-600 ml-2">
              ({product.reviewCount || 0} reviews)
            </span>
          </div>

          {/* Price */}
          <div className="mb-6">
            <div className="flex items-baseline space-x-3">
              <span className="text-4xl font-bold text-primary">
                ${product.price}
              </span>
              {product.originalPrice && product.originalPrice > product.price && (
                <>
                  <span className="text-xl text-gray-500 line-through">
                    ${product.originalPrice}
                  </span>
                  <span className="bg-red-500 text-white px-2 py-1 rounded text-sm font-semibold">
                    {Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)}% OFF
                  </span>
                </>
              )}
            </div>
          </div>

          {/* Description */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold mb-2">Description</h3>
            <p className="text-gray-700 leading-relaxed">
              {product.description || 'No description available.'}
            </p>
          </div>

          {/* SKU */}
          <p className="text-sm text-gray-600 mb-4">
            SKU: <span className="font-mono">{product.sku}</span>
          </p>

          {/* Stock Status */}
          <div className="mb-6">
            {product.stockQuantity > 0 ? (
              <div className="flex items-center space-x-2">
                <span className="text-green-600 font-semibold">In Stock</span>
                <span className="text-gray-600">
                  ({product.stockQuantity} available)
                </span>
              </div>
            ) : (
              <span className="text-red-600 font-semibold">Out of Stock</span>
            )}
          </div>

          {/* Quantity Selector */}
          {product.stockQuantity > 0 && (
            <div className="mb-6">
              <label className="block text-sm font-semibold mb-2">Quantity:</label>
              <div className="flex items-center space-x-4">
                <button
                  onClick={decreaseQuantity}
                  className="bg-gray-200 p-3 rounded-lg hover:bg-gray-300"
                >
                  <FaMinus />
                </button>
                <span className="text-2xl font-semibold w-16 text-center">
                  {quantity}
                </span>
                <button
                  onClick={increaseQuantity}
                  className="bg-gray-200 p-3 rounded-lg hover:bg-gray-300"
                >
                  <FaPlus />
                </button>
              </div>
            </div>
          )}

          {/* Action Buttons */}
          <div className="flex space-x-4 mb-6">
            <button
              onClick={handleAddToCart}
              disabled={product.stockQuantity === 0}
              className={`
                flex-1 flex items-center justify-center space-x-2 py-3 rounded-lg font-semibold text-lg
                ${product.stockQuantity === 0
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : 'bg-primary text-white hover:bg-blue-600'
                }
              `}
            >
              <FaShoppingCart />
              <span>Add to Cart</span>
            </button>

            <button className="bg-white border-2 border-primary text-primary p-3 rounded-lg hover:bg-gray-50">
              <FaHeart className="text-2xl" />
            </button>
          </div>

          {/* Additional Info */}
          <div className="border-t pt-6">
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-gray-600">Category:</span>
                <span className="font-semibold">{product.categoryName || 'N/A'}</span>
              </div>
              {product.weight && (
                <div className="flex justify-between">
                  <span className="text-gray-600">Weight:</span>
                  <span className="font-semibold">{product.weight} kg</span>
                </div>
              )}
              {product.dimensions && (
                <div className="flex justify-between">
                  <span className="text-gray-600">Dimensions:</span>
                  <span className="font-semibold">{product.dimensions}</span>
                </div>
              )}
            </div>
          </div>

          {/* Shipping Info */}
          <div className="mt-6 bg-blue-50 p-4 rounded-lg">
            <h4 className="font-semibold mb-2">Shipping Information</h4>
            <ul className="text-sm text-gray-700 space-y-1">
              <li>✓ Free shipping on orders over $50</li>
              <li>✓ Estimated delivery: 3-5 business days</li>
              <li>✓ 30-day return policy</li>
            </ul>
          </div>
        </div>
      </div>

      {/* Product Specifications */}
      <div className="px-8 pb-8">
        <div className="border-t pt-6">
          <h3 className="text-2xl font-bold mb-4">Specifications</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-sm text-gray-600">Product ID</p>
              <p className="font-semibold">{product.id}</p>
            </div>
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-sm text-gray-600">SKU</p>
              <p className="font-semibold">{product.sku}</p>
            </div>
            {product.brand && (
              <div className="bg-gray-50 p-4 rounded">
                <p className="text-sm text-gray-600">Brand</p>
                <p className="font-semibold">{product.brand}</p>
              </div>
            )}
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-sm text-gray-600">Status</p>
              <p className="font-semibold">{product.status}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;