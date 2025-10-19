package com.ecommerce.inventory.entity;

public enum MovementType {
    IN,      // Stock added
    OUT,     // Stock removed
    RESERVED, // Stock reserved for order
    RELEASED  // Reserved stock released back
}