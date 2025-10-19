package com.ecommerce.inventory.entity;

public enum MovementReason {
    PURCHASE,           // New stock purchased
    SALE,              // Sold to customer
    RETURN,            // Customer return
    DAMAGED,           // Damaged goods
    ADJUSTMENT,        // Inventory adjustment
    TRANSFER,          // Transfer between warehouses
    ORDER_RESERVATION, // Reserved for order
    ORDER_CANCELLATION // Order cancelled, stock released
}