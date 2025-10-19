package com.ecommerce.order.client;

import com.ecommerce.order.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);

    @GetMapping("/api/products/sku/{sku}")
    ProductResponseDTO getProductBySku(@PathVariable String sku);
}