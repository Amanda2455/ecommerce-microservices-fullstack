package com.ecommerce.order.dto;

import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.entity.PaymentMethod;
import com.ecommerce.order.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private List<OrderItemResponseDTO> orderItems;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String paymentTransactionId;

    // Shipping Address
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;
    private String shippingZipCode;

    // Billing Address
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingZipCode;

    private String notes;
    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}