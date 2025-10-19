package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String currency;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Email(message = "Invalid email format")
    private String customerEmail;

    private String customerPhone;

    // Card details (for CREDIT_CARD, DEBIT_CARD)
    private String cardNumber;
    private String cardHolderName;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardCvv;

    // UPI details
    private String upiId;

    // Bank details
    private String bankName;
    private String accountNumber;
    private String ifscCode;

    // Wallet details
    private String walletProvider;

    private String description;
}