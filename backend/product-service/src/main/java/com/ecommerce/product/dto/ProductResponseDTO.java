package com.ecommerce.product.dto;

import com.ecommerce.product.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer stockQuantity;
    private CategoryResponseDTO category;
    private String brand;
    private String imageUrl;
    private Double weight;
    private String dimensions;
    private ProductStatus status;
    private Boolean isFeatured;
    private Integer viewCount;
    private Integer soldCount;
    private Long sellerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}