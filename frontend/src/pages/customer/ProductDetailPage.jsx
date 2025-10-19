import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import productService from '../../services/productService';
import inventoryService from '../../services/inventoryService';
import ProductDetail from '../../components/customer/ProductDetail';
import ProductCard from '../../components/customer/ProductCard';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import { FaHome, FaChevronRight } from 'react-icons/fa';

const ProductDetailPage = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [relatedProducts, setRelatedProducts] = useState([]);
  const [stockAvailable, setStockAvailable] = useState(true);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProductDetails();
    window.scrollTo(0, 0); // Scroll to top when product changes
  }, [id]);

  const fetchProductDetails = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch product details
      const productResponse = await productService.getProductById(id);
      const productData = productResponse.data;
      setProduct(productData);

      // Check stock availability
      try {
        const stockResponse = await inventoryService.checkStockAvailability(id, 1);
        setStockAvailable(stockResponse.data);
      } catch (stockError) {
        console.error('Error checking stock:', stockError);
        setStockAvailable(false);
      }

      // Fetch related products (same category)
      if (productData.categoryId) {
        try {
          const relatedResponse = await productService.getProductsByCategory(productData.categoryId);
          // Filter out current product and limit to 4 items
          const filtered = relatedResponse.data
            .filter(p => p.id !== parseInt(id))
            .slice(0, 4);
          setRelatedProducts(filtered);
        } catch (relatedError) {
          console.error('Error fetching related products:', relatedError);
        }
      }

    } catch (err) {
      console.error('Error fetching product:', err);
      setError(err.response?.data?.message || 'Failed to load product details');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8">
        <ErrorMessage message={error} />
        <div className="text-center mt-6">
          <Link to="/products" className="text-primary hover:underline">
            ← Back to Products
          </Link>
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8 text-center">
        <h2 className="text-2xl font-bold mb-4">Product Not Found</h2>
        <Link to="/products" className="text-primary hover:underline">
          ← Back to Products
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Breadcrumb */}
      <nav className="flex items-center space-x-2 text-sm text-gray-600 mb-8">
        <Link to="/" className="hover:text-primary flex items-center">
          <FaHome className="mr-1" />
          Home
        </Link>
        <FaChevronRight className="text-xs" />
        <Link to="/products" className="hover:text-primary">
          Products
        </Link>
        {product.categoryName && (
          <>
            <FaChevronRight className="text-xs" />
            <Link 
              to={`/products?category=${product.categoryId}`} 
              className="hover:text-primary"
            >
              {product.categoryName}
            </Link>
          </>
        )}
        <FaChevronRight className="text-xs" />
        <span className="text-gray-900 font-semibold">{product.name}</span>
      </nav>

      {/* Product Detail Component */}
      <ProductDetail product={product} />

      {/* Product Tabs (Reviews, Description, Specifications) */}
      <div className="mt-12 bg-white rounded-lg shadow-md">
        <div className="border-b">
          <nav className="flex">
            <button className="px-6 py-4 border-b-2 border-primary font-semibold text-primary">
              Description
            </button>
            <button className="px-6 py-4 text-gray-600 hover:text-gray-900">
              Specifications
            </button>
            <button className="px-6 py-4 text-gray-600 hover:text-gray-900">
              Reviews ({product.reviewCount || 0})
            </button>
            <button className="px-6 py-4 text-gray-600 hover:text-gray-900">
              Shipping Info
            </button>
          </nav>
        </div>

        <div className="p-8">
          {/* Description Tab Content */}
          <div>
            <h3 className="text-xl font-bold mb-4">Product Description</h3>
            <div className="text-gray-700 leading-relaxed space-y-4">
              <p>{product.description || 'No description available.'}</p>
              
              {product.longDescription && (
                <p>{product.longDescription}</p>
              )}

              <div className="mt-6">
                <h4 className="font-semibold mb-2">Key Features:</h4>
                <ul className="list-disc list-inside space-y-1 text-gray-700">
                  <li>High-quality materials and construction</li>
                  <li>Designed for durability and performance</li>
                  <li>Easy to use and maintain</li>
                  <li>Comes with manufacturer warranty</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Related Products */}
      {relatedProducts.length > 0 && (
        <div className="mt-16">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold">Related Products</h2>
            {product.categoryId && (
              <Link
                to={`/products?category=${product.categoryId}`}
                className="text-primary hover:underline"
              >
                View All in Category →
              </Link>
            )}
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
            {relatedProducts.map((relatedProduct) => (
              <ProductCard key={relatedProduct.id} product={relatedProduct} />
            ))}
          </div>
        </div>
      )}

      {/* Recently Viewed (placeholder) */}
      <div className="mt-16 bg-gray-50 rounded-lg p-8">
        <h3 className="text-xl font-bold mb-4">Recently Viewed</h3>
        <p className="text-gray-600">Your recently viewed products will appear here.</p>
      </div>
    </div>
  );
};

export default ProductDetailPage;