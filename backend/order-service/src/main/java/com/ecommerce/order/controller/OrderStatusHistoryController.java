package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderStatusHistoryResponseDTO;
import com.ecommerce.order.service.OrderStatusHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-status-history")
@RequiredArgsConstructor
@Tag(name = "Order Status History", description = "APIs for tracking and retrieving order status change history and audit trail")
public class OrderStatusHistoryController {

    private final OrderStatusHistoryService orderStatusHistoryService;

    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Get status history for an order",
            description = "Retrieve complete status change history for a specific order, including timestamps, status transitions, and remarks. History is sorted chronologically."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order status history retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderStatusHistoryResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found with given ID"
            )
    })
    public ResponseEntity<List<OrderStatusHistoryResponseDTO>> getHistoryByOrderId(
            @Parameter(description = "Order ID to retrieve status history", example = "1", required = true)
            @PathVariable Long orderId) {
        List<OrderStatusHistoryResponseDTO> history = orderStatusHistoryService.getHistoryByOrderId(orderId);
        return ResponseEntity.ok(history);
    }

    @GetMapping
    @Operation(
            summary = "Get all order status history",
            description = "Retrieve complete status change history for all orders in the system. Useful for audit purposes and reporting. (Admin only in production)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All order status history retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = OrderStatusHistoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<OrderStatusHistoryResponseDTO>> getAllHistory() {
        List<OrderStatusHistoryResponseDTO> history = orderStatusHistoryService.getAllHistory();
        return ResponseEntity.ok(history);
    }
}
/**
package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderStatusHistoryResponseDTO;
import com.ecommerce.order.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-status-history")
@RequiredArgsConstructor
public class OrderStatusHistoryController {

    private final OrderStatusHistoryService orderStatusHistoryService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderStatusHistoryResponseDTO>> getHistoryByOrderId(@PathVariable Long orderId) {
        List<OrderStatusHistoryResponseDTO> history = orderStatusHistoryService.getHistoryByOrderId(orderId);
        return ResponseEntity.ok(history);
    }

    @GetMapping
    public ResponseEntity<List<OrderStatusHistoryResponseDTO>> getAllHistory() {
        List<OrderStatusHistoryResponseDTO> history = orderStatusHistoryService.getAllHistory();
        return ResponseEntity.ok(history);
    }
}

 */