package com.ecommerce.payment.entity;

public enum PaymentStatus {
    PENDING,        // Payment initiated
    PROCESSING,     // Payment in process
    COMPLETED,      // Payment successful
    FAILED,         // Payment failed
    REFUNDED,       // Payment refunded
    PARTIALLY_REFUNDED, // Partial refund
    CANCELLED       // Payment cancelled
}