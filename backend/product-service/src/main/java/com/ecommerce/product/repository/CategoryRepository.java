package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Optional<Category> findBySlug(String slug);

    List<Category> findByStatus(CategoryStatus status);

    List<Category> findByParentIsNull(); // Root categories

    List<Category> findByParentId(Long parentId); // Sub-categories

    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.status = 'ACTIVE'")
    List<Category> findActiveRootCategories();
}