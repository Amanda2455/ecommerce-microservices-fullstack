package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.entity.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    Optional<Inventory> findBySku(String sku);

    List<Inventory> findByWarehouseId(Long warehouseId);

    List<Inventory> findByStatus(InventoryStatus status);

    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity <= i.reorderLevel")
    List<Inventory> findLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity = 0")
    List<Inventory> findOutOfStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.productName LIKE %:keyword% OR i.sku LIKE %:keyword%")
    List<Inventory> searchInventory(@Param("keyword") String keyword);

    boolean existsByProductId(Long productId);

    boolean existsBySku(String sku);
}