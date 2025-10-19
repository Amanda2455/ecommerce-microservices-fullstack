package com.ecommerce.payment.entity;

public enum TransactionType {
    CHARGE,         // Initial payment
    REFUND,         // Full refund
    PARTIAL_REFUND, // Partial refund
    AUTHORIZATION,  // Authorization hold
    CAPTURE,        // Capture authorized amount
    VOID            // Void transaction
}