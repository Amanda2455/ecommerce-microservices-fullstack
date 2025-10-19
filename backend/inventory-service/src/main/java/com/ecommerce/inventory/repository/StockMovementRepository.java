package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.MovementType;
import com.ecommerce.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByInventoryId(Long inventoryId);

    List<StockMovement> findByMovementType(MovementType movementType);

    List<StockMovement> findByReferenceId(String referenceId);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.createdAt BETWEEN :startDate AND :endDate")
    List<StockMovement> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.inventory.id = :inventoryId ORDER BY sm.createdAt DESC")
    List<StockMovement> findByInventoryIdOrderByCreatedAtDesc(@Param("inventoryId") Long inventoryId);
}