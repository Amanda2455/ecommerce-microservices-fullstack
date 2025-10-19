package com.ecommerce.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseUpdateDTO {

    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
}