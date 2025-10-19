package com.ecommerce.inventory.dto;

import com.ecommerce.inventory.entity.MovementReason;
import com.ecommerce.inventory.entity.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponseDTO {

    private Long id;
    private Long inventoryId;
    private String productName;
    private String sku;
    private MovementType movementType;
    private Integer quantity;
    private Integer previousQuantity;
    private Integer newQuantity;
    private String referenceId;
    private MovementReason reason;
    private String notes;
    private Long performedBy;
    private LocalDateTime createdAt;
}