package com.ecommerce.inventory.dto;

import com.ecommerce.inventory.entity.WarehouseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDTO {

    private Long id;
    private String code;
    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private WarehouseStatus status;
    private Integer inventoryCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}