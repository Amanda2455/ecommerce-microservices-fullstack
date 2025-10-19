package com.ecommerce.inventory.client;

import com.ecommerce.inventory.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);

    @GetMapping("/api/products/sku/{sku}")
    ProductResponseDTO getProductBySku(@PathVariable String sku);

    /**
    @PatchMapping("/api/products/{id}/update-stock")
    void updateProductStock(@PathVariable Long id, @RequestParam Integer quantity);
    */

}