package com.ecommerce.payment.client;

import com.ecommerce.payment.dto.OrderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/orders/{id}")
    OrderResponseDTO getOrderById(@PathVariable Long id);

    @GetMapping("/api/orders/order-number/{orderNumber}")
    OrderResponseDTO getOrderByOrderNumber(@PathVariable String orderNumber);
}