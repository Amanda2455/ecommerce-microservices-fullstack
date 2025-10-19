package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.TransactionStatus;
import com.ecommerce.payment.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionResponseDTO {

    private Long id;
    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private TransactionStatus status;
    private String gatewayTransactionId;
    private String remarks;
    private LocalDateTime createdAt;
}