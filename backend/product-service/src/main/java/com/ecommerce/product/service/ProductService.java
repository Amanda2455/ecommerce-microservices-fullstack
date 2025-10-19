package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequestDTO;
import com.ecommerce.product.dto.ProductResponseDTO;
import com.ecommerce.product.dto.ProductUpdateDTO;
import com.ecommerce.product.dto.CategoryResponseDTO;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.ProductStatus;
import com.ecommerce.product.exception.InsufficientStockException;
import com.ecommerce.product.exception.ProductAlreadyExistsException;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        // Check if product with SKU already exists
        if (productRepository.existsBySku(requestDTO.getSku())) {
            throw new ProductAlreadyExistsException("Product with SKU " + requestDTO.getSku() + " already exists");
        }

        // Validate category
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId()));

        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setSku(requestDTO.getSku());
        product.setPrice(requestDTO.getPrice());
        product.setDiscountPrice(requestDTO.getDiscountPrice());
        product.setStockQuantity(requestDTO.getStockQuantity());
        product.setCategory(category);
        product.setBrand(requestDTO.getBrand());
        product.setImageUrl(requestDTO.getImageUrl());
        product.setWeight(requestDTO.getWeight());
        product.setDimensions(requestDTO.getDimensions());
        product.setIsFeatured(requestDTO.getIsFeatured() != null ? requestDTO.getIsFeatured() : false);
        product.setSellerId(requestDTO.getSellerId());

        // Set status based on stock
        if (requestDTO.getStockQuantity() > 0) {
            product.setStatus(ProductStatus.ACTIVE);
        } else {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return mapToResponseDTO(savedProduct);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Increment view count
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);

        return mapToResponseDTO(product);
    }

    public ProductResponseDTO getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return mapToResponseDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByStatus(ProductStatus status) {
        return productRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getFeaturedProducts() {
        return productRepository.findByIsFeatured(true).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getBestSellers() {
        return productRepository.findBestSellers().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getNewArrivals() {
        return productRepository.findNewArrivals().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updateDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (updateDTO.getName() != null) {
            product.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            product.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPrice() != null) {
            product.setPrice(updateDTO.getPrice());
        }
        if (updateDTO.getDiscountPrice() != null) {
            product.setDiscountPrice(updateDTO.getDiscountPrice());
        }
        if (updateDTO.getStockQuantity() != null) {
            product.setStockQuantity(updateDTO.getStockQuantity());
            // Update status based on stock
            if (updateDTO.getStockQuantity() > 0 && product.getStatus() == ProductStatus.OUT_OF_STOCK) {
                product.setStatus(ProductStatus.ACTIVE);
            } else if (updateDTO.getStockQuantity() == 0) {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }
        }
        if (updateDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + updateDTO.getCategoryId()));
            product.setCategory(category);
        }
        if (updateDTO.getBrand() != null) {
            product.setBrand(updateDTO.getBrand());
        }
        if (updateDTO.getImageUrl() != null) {
            product.setImageUrl(updateDTO.getImageUrl());
        }
        if (updateDTO.getWeight() != null) {
            product.setWeight(updateDTO.getWeight());
        }
        if (updateDTO.getDimensions() != null) {
            product.setDimensions(updateDTO.getDimensions());
        }
        if (updateDTO.getIsFeatured() != null) {
            product.setIsFeatured(updateDTO.getIsFeatured());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", id);

        return mapToResponseDTO(updatedProduct);
    }

    @Transactional
    public ProductResponseDTO updateProductStatus(Long id, ProductStatus status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);

        return mapToResponseDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getStatus() == ProductStatus.DELETED) {
            throw new IllegalStateException("Product with id " + id + " is already deleted");
        }

        product.setStatus(ProductStatus.DELETED);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);

        log.info("Product soft deleted - ID: {}", id);
    }

    /**
    // Method for Inventory Service to update stock
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(newStock);

        // Update status based on stock
        if (newStock == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (newStock > 0 && product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.ACTIVE);
        }

        productRepository.save(product);
        log.info("Stock updated for product ID: {}, New stock: {}", productId, newStock);
    }
    */

    /**
    // Method to reduce stock (called by Order Service)
    @Transactional
    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName() +
                    ". Available: " + product.getStockQuantity() + ", Requested: " + quantity);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        product.setSoldCount(product.getSoldCount() + quantity);

        if (product.getStockQuantity() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        productRepository.save(product);
        log.info("Stock reduced for product ID: {}, Quantity: {}, Remaining: {}",
                productId, quantity, product.getStockQuantity());
    }
    */


    /**
    // Method to increase stock (for order cancellation)
    @Transactional
    public void increaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.setStockQuantity(product.getStockQuantity() + quantity);

        if (product.getStatus() == ProductStatus.OUT_OF_STOCK && product.getStockQuantity() > 0) {
            product.setStatus(ProductStatus.ACTIVE);
        }

        productRepository.save(product);
        log.info("Stock increased for product ID: {}, Quantity: {}, New stock: {}",
                productId, quantity, product.getStockQuantity());
    }
    */

    private ProductResponseDTO mapToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setSku(product.getSku());
        dto.setPrice(product.getPrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(mapCategoryToSimpleDTO(product.getCategory()));
        dto.setBrand(product.getBrand());
        dto.setImageUrl(product.getImageUrl());
        dto.setWeight(product.getWeight());
        dto.setDimensions(product.getDimensions());
        dto.setStatus(product.getStatus());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setViewCount(product.getViewCount());
        dto.setSoldCount(product.getSoldCount());
        dto.setSellerId(product.getSellerId());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    private CategoryResponseDTO mapCategoryToSimpleDTO(Category category) {
        if (category == null) return null;

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        return dto;
    }

}