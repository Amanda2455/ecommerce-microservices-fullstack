
package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequestDTO;
import com.ecommerce.product.dto.ProductResponseDTO;
import com.ecommerce.product.dto.ProductUpdateDTO;
import com.ecommerce.product.entity.ProductStatus;
import com.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing product catalog, inventory, pricing, search, and product information")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Add a new product to the catalog with details like name, SKU, price, category, brand, and inventory information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Product with this SKU already exists"
            )
    })
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Parameter(description = "Product details including name, SKU, price, and inventory", required = true)
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by ID",
            description = "Retrieve complete product information including pricing, inventory, category, and seller details by product ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with given ID"
            )
    })
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable Long id) {
        ProductResponseDTO responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/sku/{sku}")
    @Operation(
            summary = "Get product by SKU",
            description = "Retrieve product information using the unique Stock Keeping Unit (SKU) identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with given SKU"
            )
    })
    public ResponseEntity<ProductResponseDTO> getProductBySku(
            @Parameter(description = "Product SKU (Stock Keeping Unit)", example = "SKU-LAPTOP-001", required = true)
            @PathVariable String sku) {
        ProductResponseDTO responseDTO = productService.getProductBySku(sku);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Retrieve a complete list of all products in the catalog"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of all products retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get products by status",
            description = "Retrieve all products filtered by status (ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products filtered by status retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStatus(
            @Parameter(description = "Product status", example = "ACTIVE", required = true)
            @PathVariable ProductStatus status) {
        List<ProductResponseDTO> products = productService.getProductsByStatus(status);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(
            summary = "Get products by category",
            description = "Retrieve all products belonging to a specific category (e.g., Electronics, Clothing, Books)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products in category retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @Parameter(description = "Category ID", example = "1", required = true)
            @PathVariable Long categoryId) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/brand/{brand}")
    @Operation(
            summary = "Get products by brand",
            description = "Retrieve all products from a specific brand or manufacturer"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products filtered by brand retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(
            @Parameter(description = "Brand name", example = "Samsung", required = true)
            @PathVariable String brand) {
        List<ProductResponseDTO> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/seller/{sellerId}")
    @Operation(
            summary = "Get products by seller",
            description = "Retrieve all products listed by a specific seller or vendor in the marketplace"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products from seller retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySeller(
            @Parameter(description = "Seller/Vendor ID", example = "1", required = true)
            @PathVariable Long sellerId) {
        List<ProductResponseDTO> products = productService.getProductsBySeller(sellerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/featured")
    @Operation(
            summary = "Get featured products",
            description = "Retrieve products marked as featured for homepage or promotional display"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Featured products retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getFeaturedProducts() {
        List<ProductResponseDTO> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search products",
            description = "Search products by keyword matching product name, description, SKU, or brand. Useful for search functionality."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(
            @Parameter(description = "Search keyword", example = "laptop", required = true)
            @RequestParam String keyword) {
        List<ProductResponseDTO> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    @Operation(
            summary = "Get products by price range",
            description = "Filter products within a specified minimum and maximum price range. Useful for price-based filtering."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products within price range retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", example = "100.00", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", example = "1000.00", required = true)
            @RequestParam BigDecimal maxPrice) {
        List<ProductResponseDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best-sellers")
    @Operation(
            summary = "Get best-selling products",
            description = "Retrieve products with highest sales volume, typically used for recommendations and trending sections"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Best-selling products retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getBestSellers() {
        List<ProductResponseDTO> products = productService.getBestSellers();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/new-arrivals")
    @Operation(
            summary = "Get new arrival products",
            description = "Retrieve recently added products, sorted by creation date (newest first)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "New arrival products retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductResponseDTO>> getNewArrivals() {
        List<ProductResponseDTO> products = productService.getNewArrivals();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update product details",
            description = "Update product information including name, description, price, category, inventory, and other attributes"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with given ID"
            )
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product details", required = true)
            @Valid @RequestBody ProductUpdateDTO updateDTO) {
        ProductResponseDTO responseDTO = productService.updateProduct(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update product status",
            description = "Change product status (e.g., ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED). Used for inventory management."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product status updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status value"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with given ID"
            )
    })
    public ResponseEntity<ProductResponseDTO> updateProductStatus(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New product status", example = "ACTIVE", required = true)
            @RequestParam ProductStatus status) {
        ProductResponseDTO responseDTO = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a product",
            description = "Permanently delete a product from the catalog. This action cannot be undone. (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Product deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Product cannot be deleted (has active orders or dependencies)"
            )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}

/**

package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequestDTO;
import com.ecommerce.product.dto.ProductResponseDTO;
import com.ecommerce.product.dto.ProductUpdateDTO;
import com.ecommerce.product.entity.ProductStatus;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getProductBySku(@PathVariable String sku) {
        ProductResponseDTO responseDTO = productService.getProductBySku(sku);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStatus(@PathVariable ProductStatus status) {
        List<ProductResponseDTO> products = productService.getProductsByStatus(status);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(@PathVariable String brand) {
        List<ProductResponseDTO> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySeller(@PathVariable Long sellerId) {
        List<ProductResponseDTO> products = productService.getProductsBySeller(sellerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponseDTO>> getFeaturedProducts() {
        List<ProductResponseDTO> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductResponseDTO> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductResponseDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<List<ProductResponseDTO>> getBestSellers() {
        List<ProductResponseDTO> products = productService.getBestSellers();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<List<ProductResponseDTO>> getNewArrivals() {
        List<ProductResponseDTO> products = productService.getNewArrivals();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {
        ProductResponseDTO responseDTO = productService.updateProduct(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponseDTO> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status) {
        ProductResponseDTO responseDTO = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}

**/