package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Warehouse code (e.g., WH001)

    @Column(nullable = false)
    private String name;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String contactPerson;

    private String contactPhone;

    private String contactEmail;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<Inventory> inventories = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarehouseStatus status = WarehouseStatus.ACTIVE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}