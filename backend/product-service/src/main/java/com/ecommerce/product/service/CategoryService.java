package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryRequestDTO;
import com.ecommerce.product.dto.CategoryResponseDTO;
import com.ecommerce.product.dto.CategoryUpdateDTO;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.CategoryStatus;
import com.ecommerce.product.exception.CategoryAlreadyExistsException;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        // Check if category already exists
        if (categoryRepository.existsByName(requestDTO.getName())) {
            throw new CategoryAlreadyExistsException("Category with name " + requestDTO.getName() + " already exists");
        }

        String slug = requestDTO.getSlug() != null ? requestDTO.getSlug() : generateSlug(requestDTO.getName());

        if (categoryRepository.existsBySlug(slug)) {
            throw new CategoryAlreadyExistsException("Category with slug " + slug + " already exists");
        }

        Category category = new Category();
        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        category.setSlug(slug);
        category.setImageUrl(requestDTO.getImageUrl());

        // Set parent category if provided
        if (requestDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + requestDTO.getParentId()));
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return mapToResponseDTO(savedCategory);
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponseDTO(category);
    }

    public CategoryResponseDTO getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return mapToResponseDTO(category);
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getRootCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getActiveRootCategories() {
        return categoryRepository.findActiveRootCategories().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getCategoriesByStatus(CategoryStatus status) {
        return categoryRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO updateDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (updateDTO.getName() != null) {
            // Check if name is being changed and if new name already exists
            if (!category.getName().equals(updateDTO.getName()) &&
                    categoryRepository.existsByName(updateDTO.getName())) {
                throw new CategoryAlreadyExistsException("Category with name " + updateDTO.getName() + " already exists");
            }
            category.setName(updateDTO.getName());
        }

        if (updateDTO.getDescription() != null) {
            category.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getSlug() != null) {
            // Check if slug is being changed and if new slug already exists
            if (!category.getSlug().equals(updateDTO.getSlug()) &&
                    categoryRepository.existsBySlug(updateDTO.getSlug())) {
                throw new CategoryAlreadyExistsException("Category with slug " + updateDTO.getSlug() + " already exists");
            }
            category.setSlug(updateDTO.getSlug());
        }

        if (updateDTO.getImageUrl() != null) {
            category.setImageUrl(updateDTO.getImageUrl());
        }

        if (updateDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(updateDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + updateDTO.getParentId()));
            category.setParent(parent);
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with ID: {}", id);

        return mapToResponseDTO(updatedCategory);
    }

    @Transactional
    public CategoryResponseDTO updateCategoryStatus(Long id, CategoryStatus status) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setStatus(status);
        Category updatedCategory = categoryRepository.save(category);

        return mapToResponseDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (category.getStatus() == CategoryStatus.DELETED) {
            throw new IllegalStateException("Category with id " + id + " is already deleted");
        }

        // Check if category has products
        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing products. Please move or delete products first.");
        }

        // Check if category has sub-categories
        if (!category.getSubCategories().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with sub-categories. Please delete sub-categories first.");
        }

        category.setStatus(CategoryStatus.DELETED);
        categoryRepository.save(category);

        log.info("Category soft deleted - ID: {}", id);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setSlug(category.getSlug());
        dto.setImageUrl(category.getImageUrl());
        dto.setStatus(category.getStatus());
        dto.setProductCount(category.getProducts() != null ? category.getProducts().size() : 0);
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }

        // Map sub-categories (simple mapping without nesting to avoid deep recursion)
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            dto.setSubCategories(category.getSubCategories().stream()
                    .map(this::mapToSimpleDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private CategoryResponseDTO mapToSimpleDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setStatus(category.getStatus());
        return dto;
    }
}