package com.ecommerce.product.controller;

import com.ecommerce.product.dto.CategoryRequestDTO;
import com.ecommerce.product.dto.CategoryResponseDTO;
import com.ecommerce.product.dto.CategoryUpdateDTO;
import com.ecommerce.product.entity.CategoryStatus;
import com.ecommerce.product.service.CategoryService;
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

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing product categories, hierarchical category structure, and category navigation")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(
            summary = "Create a new category",
            description = "Create a new product category with hierarchical support. Categories can have parent-child relationships for multi-level navigation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Category with this slug already exists"
            )
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Parameter(description = "Category details including name, slug, description, and parent category", required = true)
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO responseDTO = categoryService.createCategory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by ID",
            description = "Retrieve category details including name, description, parent category, and subcategories by category ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with given ID"
            )
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "Category ID", example = "1", required = true)
            @PathVariable Long id) {
        CategoryResponseDTO responseDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/slug/{slug}")
    @Operation(
            summary = "Get category by slug",
            description = "Retrieve category information using URL-friendly slug (e.g., 'electronics', 'clothing-mens'). Used for SEO-friendly URLs."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with given slug"
            )
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(
            @Parameter(description = "Category slug (URL-friendly identifier)", example = "electronics", required = true)
            @PathVariable String slug) {
        CategoryResponseDTO responseDTO = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Retrieve a complete list of all categories including root categories and subcategories in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of all categories retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/root")
    @Operation(
            summary = "Get root categories",
            description = "Retrieve all top-level categories (categories without a parent). Used for main navigation menus."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Root categories retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoryResponseDTO>> getRootCategories() {
        List<CategoryResponseDTO> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/root/active")
    @Operation(
            summary = "Get active root categories",
            description = "Retrieve only active top-level categories. Ideal for displaying in storefront navigation without inactive categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Active root categories retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoryResponseDTO>> getActiveRootCategories() {
        List<CategoryResponseDTO> categories = categoryService.getActiveRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{parentId}/subcategories")
    @Operation(
            summary = "Get subcategories of a parent category",
            description = "Retrieve all child categories belonging to a specific parent category. Used for building hierarchical navigation."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Subcategories retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoryResponseDTO>> getSubCategories(
            @Parameter(description = "Parent category ID", example = "1", required = true)
            @PathVariable Long parentId) {
        List<CategoryResponseDTO> categories = categoryService.getSubCategories(parentId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get categories by status",
            description = "Retrieve all categories filtered by status (ACTIVE, INACTIVE, HIDDEN, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories filtered by status retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByStatus(
            @Parameter(description = "Category status", example = "ACTIVE", required = true)
            @PathVariable CategoryStatus status) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByStatus(status);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update category details",
            description = "Update category information including name, slug, description, parent category, and display order"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Slug already exists or circular parent-child relationship detected"
            )
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "Category ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated category details", required = true)
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {
        CategoryResponseDTO responseDTO = categoryService.updateCategory(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update category status",
            description = "Change category status (e.g., ACTIVE, INACTIVE, HIDDEN). Changing status may affect product visibility in that category."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category status updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status value"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with given ID"
            )
    })
    public ResponseEntity<CategoryResponseDTO> updateCategoryStatus(
            @Parameter(description = "Category ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New category status", example = "ACTIVE", required = true)
            @RequestParam CategoryStatus status) {
        CategoryResponseDTO responseDTO = categoryService.updateCategoryStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Permanently delete a category. Categories with subcategories or associated products cannot be deleted. (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Category cannot be deleted (has subcategories or associated products)"
            )
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID", example = "1", required = true)
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}


/**

package com.ecommerce.product.controller;

import com.ecommerce.product.dto.CategoryRequestDTO;
import com.ecommerce.product.dto.CategoryResponseDTO;
import com.ecommerce.product.dto.CategoryUpdateDTO;
import com.ecommerce.product.entity.CategoryStatus;
import com.ecommerce.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO responseDTO = categoryService.createCategory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO responseDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponseDTO responseDTO = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryResponseDTO>> getRootCategories() {
        List<CategoryResponseDTO> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/root/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveRootCategories() {
        List<CategoryResponseDTO> categories = categoryService.getActiveRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryResponseDTO>> getSubCategories(@PathVariable Long parentId) {
        List<CategoryResponseDTO> categories = categoryService.getSubCategories(parentId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByStatus(@PathVariable CategoryStatus status) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByStatus(status);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {
        CategoryResponseDTO responseDTO = categoryService.updateCategory(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryResponseDTO> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam CategoryStatus status) {
        CategoryResponseDTO responseDTO = categoryService.updateCategoryStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

 */