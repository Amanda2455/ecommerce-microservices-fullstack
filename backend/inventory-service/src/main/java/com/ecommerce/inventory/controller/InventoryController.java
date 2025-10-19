package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequestDTO;
import com.ecommerce.inventory.dto.InventoryResponseDTO;
import com.ecommerce.inventory.dto.InventoryUpdateDTO;
import com.ecommerce.inventory.dto.StockAdjustmentDTO;
import com.ecommerce.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing stock levels, warehouse inventory, stock reservations, and availability checks")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(
            summary = "Create inventory record",
            description = "Create a new inventory record for a product including available quantity, reserved quantity, and warehouse location"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Inventory created successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Inventory record already exists for this product"
            )
    })
    public ResponseEntity<InventoryResponseDTO> createInventory(
            @Parameter(description = "Inventory details including product ID, quantity, and warehouse", required = true)
            @Valid @RequestBody InventoryRequestDTO requestDTO) {
        InventoryResponseDTO responseDTO = inventoryService.createInventory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get inventory by ID",
            description = "Retrieve inventory details including available stock, reserved stock, and warehouse information by inventory ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory found",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found with given ID"
            )
    })
    public ResponseEntity<InventoryResponseDTO> getInventoryById(
            @Parameter(description = "Inventory ID", example = "1", required = true)
            @PathVariable Long id) {
        InventoryResponseDTO responseDTO = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/product/{productId}")
    @Operation(
            summary = "Get inventory by product ID",
            description = "Retrieve inventory information for a specific product across all warehouses or aggregated stock levels"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory found for product",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found for given product ID"
            )
    })
    public ResponseEntity<InventoryResponseDTO> getInventoryByProductId(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable Long productId) {
        InventoryResponseDTO responseDTO = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/sku/{sku}")
    @Operation(
            summary = "Get inventory by SKU",
            description = "Retrieve inventory information using product SKU (Stock Keeping Unit)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory found for SKU",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found for given SKU"
            )
    })
    public ResponseEntity<InventoryResponseDTO> getInventoryBySku(
            @Parameter(description = "Product SKU", example = "SKU-LAPTOP-001", required = true)
            @PathVariable String sku) {
        InventoryResponseDTO responseDTO = inventoryService.getInventoryBySku(sku);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all inventory records",
            description = "Retrieve a complete list of all inventory records across all products and warehouses"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All inventory records retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        List<InventoryResponseDTO> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(
            summary = "Get inventory by warehouse",
            description = "Retrieve all inventory records for a specific warehouse or storage location"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse inventory retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<InventoryResponseDTO>> getInventoryByWarehouse(
            @Parameter(description = "Warehouse ID", example = "1", required = true)
            @PathVariable Long warehouseId) {
        List<InventoryResponseDTO> inventory = inventoryService.getInventoryByWarehouse(warehouseId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/low-stock")
    @Operation(
            summary = "Get low stock items",
            description = "Retrieve products with stock levels below the minimum threshold. Used for reorder alerts and inventory planning."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Low stock items retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<InventoryResponseDTO>> getLowStockItems() {
        List<InventoryResponseDTO> inventory = inventoryService.getLowStockItems();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/out-of-stock")
    @Operation(
            summary = "Get out of stock items",
            description = "Retrieve products with zero available quantity. Critical for inventory management and customer notifications."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Out of stock items retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<InventoryResponseDTO>> getOutOfStockItems() {
        List<InventoryResponseDTO> inventory = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search inventory",
            description = "Search inventory records by keyword matching product name, SKU, or other attributes"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
            )
    )
    public ResponseEntity<List<InventoryResponseDTO>> searchInventory(
            @Parameter(description = "Search keyword", example = "laptop", required = true)
            @RequestParam String keyword) {
        List<InventoryResponseDTO> inventory = inventoryService.searchInventory(keyword);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update inventory details",
            description = "Update inventory information including warehouse location, minimum stock threshold, and other attributes"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory updated successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found with given ID"
            )
    })
    public ResponseEntity<InventoryResponseDTO> updateInventory(
            @Parameter(description = "Inventory ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated inventory details", required = true)
            @Valid @RequestBody InventoryUpdateDTO updateDTO) {
        InventoryResponseDTO responseDTO = inventoryService.updateInventory(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/add-stock")
    @Operation(
            summary = "Add stock to inventory",
            description = "Increase available stock quantity. Used for receiving new shipments or restocking. Includes reason and reference tracking."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock added successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid quantity or adjustment details"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found with given ID"
            )
    })
    public ResponseEntity<InventoryResponseDTO> addStock(
            @Parameter(description = "Inventory ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Stock adjustment details including quantity and reason", required = true)
            @Valid @RequestBody StockAdjustmentDTO adjustmentDTO) {
        InventoryResponseDTO responseDTO = inventoryService.addStock(id, adjustmentDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/remove-stock")
    @Operation(
            summary = "Remove stock from inventory",
            description = "Decrease available stock quantity. Used for damaged goods, returns to supplier, or manual adjustments. Includes audit trail."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock removed successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid quantity or insufficient stock"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found with given ID"
            )
    })
    public ResponseEntity<InventoryResponseDTO> removeStock(
            @Parameter(description = "Inventory ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Stock adjustment details including quantity and reason", required = true)
            @Valid @RequestBody StockAdjustmentDTO adjustmentDTO) {
        InventoryResponseDTO responseDTO = inventoryService.removeStock(id, adjustmentDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete inventory record",
            description = "Permanently delete an inventory record. Only allowed for products with zero stock and no reservations. (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Inventory deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Inventory cannot be deleted (has stock or reservations)"
            )
    })
    public ResponseEntity<Void> deleteInventory(
            @Parameter(description = "Inventory ID", example = "1", required = true)
            @PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    // Internal endpoints for Order Service
    @PostMapping("/reserve")
    @Operation(
            summary = "Reserve stock for order",
            description = "Reserve inventory for an order. Moves quantity from available to reserved. Used during order creation. (Internal API for Order Service)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock reserved successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Insufficient stock available"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    public ResponseEntity<InventoryResponseDTO> reserveStock(
            @Parameter(description = "Product ID", example = "1", required = true)
            @RequestParam Long productId,
            @Parameter(description = "Quantity to reserve", example = "5", required = true)
            @RequestParam Integer quantity,
            @Parameter(description = "Order ID for tracking", example = "ORD-20240115-00001", required = true)
            @RequestParam String orderId) {
        InventoryResponseDTO responseDTO = inventoryService.reserveStock(productId, quantity, orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/release")
    @Operation(
            summary = "Release reserved stock",
            description = "Release previously reserved stock back to available inventory. Used when order is cancelled. (Internal API for Order Service)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserved stock released successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid quantity or reservation not found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    public ResponseEntity<InventoryResponseDTO> releaseReservedStock(
            @Parameter(description = "Product ID", example = "1", required = true)
            @RequestParam Long productId,
            @Parameter(description = "Quantity to release", example = "5", required = true)
            @RequestParam Integer quantity,
            @Parameter(description = "Order ID for tracking", example = "ORD-20240115-00001", required = true)
            @RequestParam String orderId) {
        InventoryResponseDTO responseDTO = inventoryService.releaseReservedStock(productId, quantity, orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/confirm")
    @Operation(
            summary = "Confirm stock reservation",
            description = "Confirm and deduct reserved stock from inventory. Used when order is confirmed and shipped. (Internal API for Order Service)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservation confirmed and stock deducted successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid quantity or reservation not found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    public ResponseEntity<InventoryResponseDTO> confirmReservation(
            @Parameter(description = "Product ID", example = "1", required = true)
            @RequestParam Long productId,
            @Parameter(description = "Quantity to confirm", example = "5", required = true)
            @RequestParam Integer quantity,
            @Parameter(description = "Order ID for tracking", example = "ORD-20240115-00001", required = true)
            @RequestParam String orderId) {
        InventoryResponseDTO responseDTO = inventoryService.confirmReservation(productId, quantity, orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/check-availability")
    @Operation(
            summary = "Check stock availability",
            description = "Verify if sufficient stock is available for a given product and quantity. Used for real-time availability checks. (Internal/Public API)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Availability check completed",
            content = @Content(schema = @Schema(implementation = Boolean.class))
    )
    public ResponseEntity<Boolean> checkStockAvailability(
            @Parameter(description = "Product ID", example = "1", required = true)
            @RequestParam Long productId,
            @Parameter(description = "Required quantity", example = "5", required = true)
            @RequestParam Integer quantity) {
        boolean available = inventoryService.checkStockAvailability(productId, quantity);
        return ResponseEntity.ok(available);
    }
}

/**

package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequestDTO;
import com.ecommerce.inventory.dto.InventoryResponseDTO;
import com.ecommerce.inventory.dto.InventoryUpdateDTO;
import com.ecommerce.inventory.dto.StockAdjustmentDTO;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO requestDTO) {
        InventoryResponseDTO responseDTO = inventoryService.createInventory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Long id) {
        InventoryResponseDTO responseDTO = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryByProductId(@PathVariable Long productId) {
        InventoryResponseDTO responseDTO = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryResponseDTO> getInventoryBySku(@PathVariable String sku) {
        InventoryResponseDTO responseDTO = inventoryService.getInventoryBySku(sku);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        List<InventoryResponseDTO> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryResponseDTO>> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        List<InventoryResponseDTO> inventory = inventoryService.getInventoryByWarehouse(warehouseId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponseDTO>> getLowStockItems() {
        List<InventoryResponseDTO> inventory = inventoryService.getLowStockItems();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<InventoryResponseDTO>> getOutOfStockItems() {
        List<InventoryResponseDTO> inventory = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/search")
    public ResponseEntity<List<InventoryResponseDTO>> searchInventory(@RequestParam String keyword) {
        List<InventoryResponseDTO> inventory = inventoryService.searchInventory(keyword);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryUpdateDTO updateDTO) {
        InventoryResponseDTO responseDTO = inventoryService.updateInventory(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/add-stock")
    public ResponseEntity<InventoryResponseDTO> addStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentDTO adjustmentDTO) {
        InventoryResponseDTO responseDTO = inventoryService.addStock(id, adjustmentDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/remove-stock")
    public ResponseEntity<InventoryResponseDTO> removeStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentDTO adjustmentDTO) {
        InventoryResponseDTO responseDTO = inventoryService.removeStock(id, adjustmentDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    // Internal endpoints for Order Service
    @PostMapping("/reserve")
    public ResponseEntity<InventoryResponseDTO> reserveStock(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam String orderId) {
        InventoryResponseDTO responseDTO = inventoryService.reserveStock(productId, quantity, orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/release")
    public ResponseEntity<InventoryResponseDTO> releaseReservedStock(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam String orderId) {
        InventoryResponseDTO responseDTO = inventoryService.releaseReservedStock(productId, quantity, orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/confirm")
    public ResponseEntity<InventoryResponseDTO> confirmReservation(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam String orderId) {
        InventoryResponseDTO responseDTO = inventoryService.confirmReservation(productId, quantity, orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkStockAvailability(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        boolean available = inventoryService.checkStockAvailability(productId, quantity);
        return ResponseEntity.ok(available);
    }
}

 */