package com.ecommerce.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentId; // e.g., PAY-20240115-001

    @Column(nullable = false)
    private Long orderId; // Reference to Order Service

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    private String gatewayTransactionId; // Transaction ID from payment gateway

    private String gatewayResponse; // Raw response from gateway

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentTransaction> transactions = new ArrayList<>();

    // Card details (last 4 digits for display)
    private String cardLast4Digits;
    private String cardBrand; // VISA, MASTERCARD, etc.

    // UPI details
    private String upiId;

    // Bank details
    private String bankName;
    private String accountNumber; // Last 4 digits only

    // Wallet details
    private String walletProvider;

    private String customerEmail;
    private String customerPhone;

    private String description;

    private String failureReason;

    private LocalDateTime paidAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper method to add transaction
    public void addTransaction(PaymentTransaction transaction) {
        transactions.add(transaction);
        transaction.setPayment(this);
    }
}