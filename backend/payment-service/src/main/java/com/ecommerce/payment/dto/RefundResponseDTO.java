package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.RefundReason;
import com.ecommerce.payment.entity.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponseDTO {

    private Long id;
    private String refundId;
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private BigDecimal refundedAmount;
    private RefundStatus status;
    private RefundReason reason;
    private String gatewayRefundId;
    private String remarks;
    private Long initiatedBy;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}