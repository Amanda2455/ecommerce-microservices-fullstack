package com.ecommerce.order.entity;

public enum PaymentStatus {
    PENDING,        // Awaiting payment
    PROCESSING,     // Payment in process
    COMPLETED,      // Payment successful
    FAILED,         // Payment failed
    REFUNDED        // Payment refunded
}