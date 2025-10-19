/**

package com.ecommerce.order.controller;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;



    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO responseDTO = orderService.createOrder(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO responseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<OrderResponseDTO> getOrderByOrderNumber(@PathVariable String orderNumber) {
        OrderResponseDTO responseDTO = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderResponseDTO> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByEmail(@PathVariable String email) {
        List<OrderResponseDTO> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateDTO updateDTO) {
        OrderResponseDTO responseDTO = orderService.updateOrderStatus(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderCancellationDTO cancellationDTO) {
        OrderResponseDTO responseDTO = orderService.cancelOrder(id, cancellationDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}

*/

package com.ecommerce.order.controller;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing customer orders, order status, and order workflow")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(
            summary = "Create a new order",
            description = "Create a new order with one or more products. This will reserve stock in inventory and calculate total amount including tax and shipping."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation errors"),
            @ApiResponse(responseCode = "404", description = "User or Product not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient stock for one or more products")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(description = "Order details including customer info and items", required = true)
            @Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO responseDTO = orderService.createOrder(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get order by ID",
            description = "Retrieve complete order details including all order items, customer information, and current status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found with given ID")
    })
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @Parameter(description = "Order ID", example = "1", required = true)
            @PathVariable Long id) {
        OrderResponseDTO responseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/order-number/{orderNumber}")
    @Operation(
            summary = "Get order by order number",
            description = "Retrieve order details using the unique order number (e.g., ORD-20240115-00001)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found with given order number")
    })
    public ResponseEntity<OrderResponseDTO> getOrderByOrderNumber(
            @Parameter(description = "Unique order number", example = "ORD-20240115-00001", required = true)
            @PathVariable String orderNumber) {
        OrderResponseDTO responseDTO = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all orders",
            description = "Retrieve a list of all orders in the system (Admin only in production)"
    )
    @ApiResponse(responseCode = "200", description = "List of all orders retrieved successfully")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get orders by user ID",
            description = "Retrieve all orders placed by a specific user, sorted by creation date (newest first)"
    )
    @ApiResponse(responseCode = "200", description = "User orders retrieved successfully")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get orders by status",
            description = "Retrieve all orders with a specific status (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, etc.)"
    )
    @ApiResponse(responseCode = "200", description = "Orders filtered by status")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(
            @Parameter(description = "Order status", example = "PENDING", required = true)
            @PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(
            summary = "Get user orders by status",
            description = "Retrieve orders for a specific user filtered by order status"
    )
    @ApiResponse(responseCode = "200", description = "Filtered orders retrieved successfully")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserAndStatus(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Order status", example = "CONFIRMED", required = true)
            @PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get orders by date range",
            description = "Retrieve orders created within a specified date range. Useful for generating reports."
    )
    @ApiResponse(responseCode = "200", description = "Orders within date range retrieved")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByDateRange(
            @Parameter(description = "Start date", example = "2024-01-01T00:00:00", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", example = "2024-12-31T23:59:59", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderResponseDTO> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Get orders by customer email",
            description = "Retrieve all orders placed using a specific email address"
    )
    @ApiResponse(responseCode = "200", description = "Orders found for email")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByEmail(
            @Parameter(description = "Customer email", example = "john.doe@example.com", required = true)
            @PathVariable String email) {
        List<OrderResponseDTO> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update order status",
            description = "Change order status (e.g., PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED). Status transitions are validated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @Parameter(description = "Order ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status and optional remarks", required = true)
            @Valid @RequestBody OrderStatusUpdateDTO updateDTO) {
        OrderResponseDTO responseDTO = orderService.updateOrderStatus(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel an order",
            description = "Cancel a PENDING or CONFIRMED order. This will release reserved inventory stock. Shipped orders cannot be cancelled."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled in current status"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @Parameter(description = "Order ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Cancellation reason and details", required = true)
            @Valid @RequestBody OrderCancellationDTO cancellationDTO) {
        OrderResponseDTO responseDTO = orderService.cancelOrder(id, cancellationDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an order",
            description = "Permanently delete a CANCELLED order. Only cancelled orders can be deleted."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be deleted (not in CANCELLED status)"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "Order ID", example = "1", required = true)
            @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}