package com.ecommerce.product.dto;

import com.ecommerce.product.entity.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String slug;
    private Long parentId;
    private String parentName;
    private List<CategoryResponseDTO> subCategories;
    private String imageUrl;
    private CategoryStatus status;
    private Integer productCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}