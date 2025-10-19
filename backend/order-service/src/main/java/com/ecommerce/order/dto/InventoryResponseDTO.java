package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}