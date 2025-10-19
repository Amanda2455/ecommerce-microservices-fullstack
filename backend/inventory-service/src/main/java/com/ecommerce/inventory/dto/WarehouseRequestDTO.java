package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequestDTO {

    @NotBlank(message = "Warehouse code is required")
    private String code;

    @NotBlank(message = "Warehouse name is required")
    private String name;

    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String contactPerson;
    private String contactPhone;

    @Email(message = "Invalid email format")
    private String contactEmail;
}