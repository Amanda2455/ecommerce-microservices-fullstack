package com.ecommerce.inventory.dto;

import com.ecommerce.inventory.entity.MovementReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentDTO {

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Reason is required")
    private MovementReason reason;

    private String referenceId;
    private String notes;
    private Long performedBy;
}