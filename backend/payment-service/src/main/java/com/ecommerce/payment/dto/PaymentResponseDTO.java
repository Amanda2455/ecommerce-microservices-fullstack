package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.PaymentGateway;
import com.ecommerce.payment.entity.PaymentMethod;
import com.ecommerce.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    private String paymentId;
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private PaymentGateway paymentGateway;
    private String gatewayTransactionId;
    private String cardLast4Digits;
    private String cardBrand;
    private String upiId;
    private String bankName;
    private String walletProvider;
    private String customerEmail;
    private String customerPhone;
    private String description;
    private String failureReason;
    private LocalDateTime paidAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PaymentTransactionResponseDTO> transactions;
}