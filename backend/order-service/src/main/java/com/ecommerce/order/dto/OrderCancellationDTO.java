package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancellationDTO {

    @NotBlank(message = "Cancellation reason is required")
    private String reason;

    private Long cancelledBy;
}