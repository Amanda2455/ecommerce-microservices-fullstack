package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.RefundReason;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Reason is required")
    private RefundReason reason;

    private String remarks;

    private Long initiatedBy;
}