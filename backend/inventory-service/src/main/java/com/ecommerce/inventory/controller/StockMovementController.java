package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.StockMovementResponseDTO;
import com.ecommerce.inventory.entity.MovementType;
import com.ecommerce.inventory.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
@Tag(name = "Stock Movement Tracking", description = "APIs for tracking and auditing inventory stock movements, adjustments, and transaction history")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get stock movement by ID",
            description = "Retrieve detailed information about a specific stock movement transaction including type, quantity, timestamp, and reference details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock movement found",
                    content = @Content(schema = @Schema(implementation = StockMovementResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock movement not found with given ID"
            )
    })
    public ResponseEntity<StockMovementResponseDTO> getMovementById(
            @Parameter(description = "Stock movement ID", example = "1", required = true)
            @PathVariable Long id) {
        StockMovementResponseDTO responseDTO = stockMovementService.getMovementById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all stock movements",
            description = "Retrieve complete history of all stock movements across all products and warehouses. Used for comprehensive audit trails and reporting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "All stock movements retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockMovementResponseDTO.class))
            )
    )
    public ResponseEntity<List<StockMovementResponseDTO>> getAllMovements() {
        List<StockMovementResponseDTO> movements = stockMovementService.getAllMovements();
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/inventory/{inventoryId}")
    @Operation(
            summary = "Get stock movements by inventory",
            description = "Retrieve all stock movement transactions for a specific inventory item. Shows complete transaction history including additions, removals, and adjustments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Stock movements for inventory retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockMovementResponseDTO.class))
            )
    )
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByInventory(
            @Parameter(description = "Inventory ID", example = "1", required = true)
            @PathVariable Long inventoryId) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByInventory(inventoryId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/type/{movementType}")
    @Operation(
            summary = "Get stock movements by type",
            description = "Filter stock movements by transaction type (STOCK_IN, STOCK_OUT, ADJUSTMENT, TRANSFER, RESERVATION, SALE, RETURN, DAMAGE, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Stock movements filtered by type retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockMovementResponseDTO.class))
            )
    )
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByType(
            @Parameter(description = "Movement type", example = "STOCK_IN", required = true)
            @PathVariable MovementType movementType) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByType(movementType);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/reference/{referenceId}")
    @Operation(
            summary = "Get stock movements by reference",
            description = "Retrieve stock movements associated with a specific reference (order ID, purchase order, transfer ID, etc.). Used for tracking related transactions."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Stock movements for reference retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockMovementResponseDTO.class))
            )
    )
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByReference(
            @Parameter(description = "Reference ID (order ID, PO number, etc.)", example = "ORD-20240115-00001", required = true)
            @PathVariable String referenceId) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByReference(referenceId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get stock movements by date range",
            description = "Retrieve stock movements within a specified date range. Essential for generating inventory reports, reconciliation, and period analysis."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Stock movements within date range retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockMovementResponseDTO.class))
            )
    )
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByDateRange(
            @Parameter(description = "Start date and time", example = "2024-01-01T00:00:00", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time", example = "2024-12-31T23:59:59", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(movements);
    }
}


/**

package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.StockMovementResponseDTO;
import com.ecommerce.inventory.entity.MovementType;
import com.ecommerce.inventory.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> getMovementById(@PathVariable Long id) {
        StockMovementResponseDTO responseDTO = stockMovementService.getMovementById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<StockMovementResponseDTO>> getAllMovements() {
        List<StockMovementResponseDTO> movements = stockMovementService.getAllMovements();
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByInventory(@PathVariable Long inventoryId) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByInventory(inventoryId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/type/{movementType}")
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByType(@PathVariable MovementType movementType) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByType(movementType);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/reference/{referenceId}")
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByReference(@PathVariable String referenceId) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByReference(referenceId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<StockMovementResponseDTO>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<StockMovementResponseDTO> movements = stockMovementService.getMovementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(movements);
    }
}

 */