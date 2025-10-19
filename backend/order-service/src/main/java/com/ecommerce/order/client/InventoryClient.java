package com.ecommerce.order.client;

import com.ecommerce.order.dto.InventoryResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/product/{productId}")
    InventoryResponseDTO getInventoryByProductId(@RequestParam Long productId);

    @GetMapping("/api/inventory/check-availability")
    Boolean checkStockAvailability(@RequestParam Long productId, @RequestParam Integer quantity);

    @PostMapping("/api/inventory/reserve")
    InventoryResponseDTO reserveStock(@RequestParam Long productId,
                                      @RequestParam Integer quantity,
                                      @RequestParam String orderId);

    @PostMapping("/api/inventory/release")
    InventoryResponseDTO releaseReservedStock(@RequestParam Long productId,
                                              @RequestParam Integer quantity,
                                              @RequestParam String orderId);

    @PostMapping("/api/inventory/confirm")
    InventoryResponseDTO confirmReservation(@RequestParam Long productId,
                                            @RequestParam Integer quantity,
                                            @RequestParam String orderId);
}