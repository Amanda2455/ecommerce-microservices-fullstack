package com.ecommerce.order.dto;

import com.ecommerce.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistoryResponseDTO {

    private Long id;
    private Long orderId;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private String remarks;
    private Long changedBy;
    private LocalDateTime createdAt;
}