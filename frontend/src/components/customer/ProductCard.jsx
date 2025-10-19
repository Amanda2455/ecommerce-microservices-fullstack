import React from 'react';
import { Link } from 'react-router-dom';
import { FaShoppingCart, FaStar } from 'react-icons/fa';
import { useCart } from '../../context/CartContext';
import { toast } from 'react-toastify';

const ProductCard = ({ product }) => {
  const { addToCart } = useCart();

  const handleAddToCart = (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (product.stockQuantity === 0) {
      toast.error('Product is out of stock!');
      return;
    }
    
    addToCart(product, 1);
    toast.success(`${product.name} added to cart!`);
  };

  return (
    <Link to={`/products/${product.id}`} className="block">
      <div className="bg-white rounded-lg shadow-md hover:shadow-xl transition-shadow duration-300 overflow-hidden h-full flex flex-col">
        {/* Product Image */}
        <div className="relative h-48 bg-gray-200">
          <img
            src={product.imageUrl || 'https://via.placeholder.com/300'}
            alt={product.name}
            className="w-full h-full object-cover"
          />
          {product.featured && (
            <span className="absolute top-2 left-2 bg-yellow-400 text-xs font-bold px-2 py-1 rounded">
              FEATURED
            </span>
          )}
          {product.stockQuantity === 0 && (
            <span className="absolute top-2 right-2 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded">
              OUT OF STOCK
            </span>
          )}
        </div>

        {/* Product Info */}
        <div className="p-4 flex-1 flex flex-col">
          <h3 className="text-lg font-semibold text-gray-800 mb-2 line-clamp-2">
            {product.name}
          </h3>
          
          <p className="text-sm text-gray-600 mb-2 line-clamp-2 flex-1">
            {product.description}
          </p>

          {/* Brand */}
          {product.brand && (
            <p className="text-xs text-gray-500 mb-2">
              Brand: <span className="font-semibold">{product.brand}</span>
            </p>
          )}

          {/* Rating */}
          <div className="flex items-center mb-2">
            {[...Array(5)].map((_, i) => (
              <FaStar
                key={i}
                className={`text-sm ${
                  i < (product.rating || 0) ? 'text-yellow-400' : 'text-gray-300'
                }`}
              />
            ))}
            <span className="text-xs text-gray-600 ml-2">
              ({product.reviewCount || 0})
            </span>
          </div>

          {/* Price and Action */}
          <div className="flex items-center justify-between mt-auto">
            <div>
              <p className="text-2xl font-bold text-primary">
                ${product.price}
              </p>
              {product.originalPrice && product.originalPrice > product.price && (
                <p className="text-sm text-gray-500 line-through">
                  ${product.originalPrice}
                </p>
              )}
            </div>

            <button
              onClick={handleAddToCart}
              disabled={product.stockQuantity === 0}
              className={`
                flex items-center space-x-2 px-4 py-2 rounded-lg font-semibold
                ${product.stockQuantity === 0
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : 'bg-primary text-white hover:bg-blue-600'
                }
              `}
            >
              <FaShoppingCart />
              <span>Add</span>
            </button>
          </div>

          {/* Stock Info */}
          <p className="text-xs mt-2">
            {product.stockQuantity > 0 ? (
              <span className="text-green-600">
                In Stock ({product.stockQuantity} available)
              </span>
            ) : (
              <span className="text-red-600">Out of Stock</span>
            )}
          </p>
        </div>
      </div>
    </Link>
  );
};

export default ProductCard;