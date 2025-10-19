package com.ecommerce.inventory.dto;

import com.ecommerce.inventory.entity.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String sku;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer totalQuantity;
    private Integer reorderLevel;
    private Integer reorderQuantity;
    private WarehouseResponseDTO warehouse;
    private InventoryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastRestockedAt;
}