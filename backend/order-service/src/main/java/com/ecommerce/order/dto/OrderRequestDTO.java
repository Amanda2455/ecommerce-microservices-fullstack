package com.ecommerce.order.dto;

import com.ecommerce.order.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Customer phone is required")
    private String customerPhone;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemRequestDTO> orderItems;

    private BigDecimal discountAmount;

    private BigDecimal shippingFee;

    private PaymentMethod paymentMethod;

    // Shipping Address
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Shipping city is required")
    private String shippingCity;

    @NotBlank(message = "Shipping state is required")
    private String shippingState;

    @NotBlank(message = "Shipping country is required")
    private String shippingCountry;

    @NotBlank(message = "Shipping zip code is required")
    private String shippingZipCode;

    // Billing Address (optional, defaults to shipping)
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingZipCode;

    private String notes;
}