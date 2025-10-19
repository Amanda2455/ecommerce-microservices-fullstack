package com.ecommerce.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdateDTO {

    private Integer availableQuantity;
    private Integer reorderLevel;
    private Integer reorderQuantity;
    private Long warehouseId;
}