package com.ecommerce.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
@Tag(name = "Fallback & Circuit Breaker", description = "Fallback endpoints triggered when microservices are unavailable. Provides graceful degradation and error responses during service outages or circuit breaker activation.")
public class FallbackController {

    @GetMapping("/users")
    @PostMapping("/users")
    @Operation(
            summary = "User Service fallback",
            description = "Fallback response when User Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - User Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"User Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        return createFallbackResponse("User Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/products")
    @PostMapping("/products")
    @Operation(
            summary = "Product Service fallback",
            description = "Fallback response when Product Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Product Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Product Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> productServiceFallback() {
        return createFallbackResponse("Product Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/categories")
    @PostMapping("/categories")
    @Operation(
            summary = "Category Service fallback",
            description = "Fallback response when Category Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Category Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Category Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> categoryServiceFallback() {
        return createFallbackResponse("Category Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/inventory")
    @PostMapping("/inventory")
    @Operation(
            summary = "Inventory Service fallback",
            description = "Fallback response when Inventory Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Inventory Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Inventory Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> inventoryServiceFallback() {
        return createFallbackResponse("Inventory Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/warehouses")
    @PostMapping("/warehouses")
    @Operation(
            summary = "Warehouse Service fallback",
            description = "Fallback response when Warehouse Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Warehouse Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Warehouse Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> warehouseServiceFallback() {
        return createFallbackResponse("Warehouse Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/stock-movements")
    @PostMapping("/stock-movements")
    @Operation(
            summary = "Stock Movement Service fallback",
            description = "Fallback response when Stock Movement Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Stock Movement Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Stock Movement Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> stockMovementServiceFallback() {
        return createFallbackResponse("Stock Movement Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/orders")
    @PostMapping("/orders")
    @Operation(
            summary = "Order Service fallback",
            description = "Fallback response when Order Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Order Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Order Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        return createFallbackResponse("Order Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/order-status-history")
    @PostMapping("/order-status-history")
    @Operation(
            summary = "Order Status History Service fallback",
            description = "Fallback response when Order Status History Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Order Status History Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Order Status History Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> orderStatusHistoryServiceFallback() {
        return createFallbackResponse("Order Status History Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/payments")
    @PostMapping("/payments")
    @Operation(
            summary = "Payment Service fallback",
            description = "Fallback response when Payment Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Payment Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Payment Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        return createFallbackResponse("Payment Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/refunds")
    @PostMapping("/refunds")
    @Operation(
            summary = "Refund Service fallback",
            description = "Fallback response when Refund Service is unavailable due to timeout, service down, or circuit breaker open state. Triggered automatically by API Gateway."
    )
    @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Refund Service is down or not responding",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"status\": 503,\n" +
                                    "  \"error\": \"Service Unavailable\",\n" +
                                    "  \"message\": \"Refund Service is currently unavailable. Please try again later.\",\n" +
                                    "  \"fallback\": true\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> refundServiceFallback() {
        return createFallbackResponse("Refund Service is currently unavailable. Please try again later.");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", message);
        response.put("fallback", true);

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}



/**

package com.ecommerce.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        return createFallbackResponse("User Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/products")
    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> productServiceFallback() {
        return createFallbackResponse("Product Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/categories")
    @PostMapping("/categories")
    public ResponseEntity<Map<String, Object>> categoryServiceFallback() {
        return createFallbackResponse("Category Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/inventory")
    @PostMapping("/inventory")
    public ResponseEntity<Map<String, Object>> inventoryServiceFallback() {
        return createFallbackResponse("Inventory Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/warehouses")
    @PostMapping("/warehouses")
    public ResponseEntity<Map<String, Object>> warehouseServiceFallback() {
        return createFallbackResponse("Warehouse Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/stock-movements")
    @PostMapping("/stock-movements")
    public ResponseEntity<Map<String, Object>> stockMovementServiceFallback() {
        return createFallbackResponse("Stock Movement Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/orders")
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        return createFallbackResponse("Order Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/order-status-history")
    @PostMapping("/order-status-history")
    public ResponseEntity<Map<String, Object>> orderStatusHistoryServiceFallback() {
        return createFallbackResponse("Order Status History Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/payments")
    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        return createFallbackResponse("Payment Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/refunds")
    @PostMapping("/refunds")
    public ResponseEntity<Map<String, Object>> refundServiceFallback() {
        return createFallbackResponse("Refund Service is currently unavailable. Please try again later.");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", message);
        response.put("fallback", true);

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}

 */