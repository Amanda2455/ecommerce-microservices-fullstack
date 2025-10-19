package com.ecommerce.order.entity;

public enum OrderStatus {
    PENDING,        // Order created, awaiting payment
    CONFIRMED,      // Payment confirmed
    PROCESSING,     // Order is being prepared
    SHIPPED,        // Order shipped
    DELIVERED,      // Order delivered
    CANCELLED,      // Order cancelled
    RETURNED,       // Order returned
    REFUNDED        // Order refunded
}