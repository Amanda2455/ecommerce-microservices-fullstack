package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType movementType;

    @Column(nullable = false)
    private Integer quantity;

    private Integer previousQuantity;

    private Integer newQuantity;

    private String referenceId; // Order ID, Purchase Order ID, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementReason reason;

    private String notes;

    private Long performedBy; // User ID who performed the action

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}