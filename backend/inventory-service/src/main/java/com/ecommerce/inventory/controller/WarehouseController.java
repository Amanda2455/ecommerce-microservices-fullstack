package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.WarehouseRequestDTO;
import com.ecommerce.inventory.dto.WarehouseResponseDTO;
import com.ecommerce.inventory.dto.WarehouseUpdateDTO;
import com.ecommerce.inventory.entity.WarehouseStatus;
import com.ecommerce.inventory.service.WarehouseService;
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
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse Management", description = "APIs for managing warehouse locations, storage facilities, and distribution centers")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(
            summary = "Create a new warehouse",
            description = "Register a new warehouse or distribution center with details like name, code, address, capacity, and contact information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Warehouse created successfully",
                    content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Warehouse with this code already exists"
            )
    })
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(
            @Parameter(description = "Warehouse details including name, code, address, and capacity", required = true)
            @Valid @RequestBody WarehouseRequestDTO requestDTO) {
        WarehouseResponseDTO responseDTO = warehouseService.createWarehouse(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get warehouse by ID",
            description = "Retrieve complete warehouse information including name, location, capacity, and operational status by warehouse ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Warehouse found",
                    content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Warehouse not found with given ID"
            )
    })
    public ResponseEntity<WarehouseResponseDTO> getWarehouseById(
            @Parameter(description = "Warehouse ID", example = "1", required = true)
            @PathVariable Long id) {
        WarehouseResponseDTO responseDTO = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/code/{code}")
    @Operation(
            summary = "Get warehouse by code",
            description = "Retrieve warehouse information using the unique warehouse code identifier (e.g., WH-NYC-001, WH-LA-002)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Warehouse found",
                    content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Warehouse not found with given code"
            )
    })
    public ResponseEntity<WarehouseResponseDTO> getWarehouseByCode(
            @Parameter(description = "Unique warehouse code", example = "WH-NYC-001", required = true)
            @PathVariable String code) {
        WarehouseResponseDTO responseDTO = warehouseService.getWarehouseByCode(code);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all warehouses",
            description = "Retrieve a complete list of all warehouses and distribution centers in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of all warehouses retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = WarehouseResponseDTO.class))
            )
    )
    public ResponseEntity<List<WarehouseResponseDTO>> getAllWarehouses() {
        List<WarehouseResponseDTO> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get warehouses by status",
            description = "Retrieve all warehouses filtered by operational status (ACTIVE, INACTIVE, MAINTENANCE, CLOSED, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouses filtered by status retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = WarehouseResponseDTO.class))
            )
    )
    public ResponseEntity<List<WarehouseResponseDTO>> getWarehousesByStatus(
            @Parameter(description = "Warehouse operational status", example = "ACTIVE", required = true)
            @PathVariable WarehouseStatus status) {
        List<WarehouseResponseDTO> warehouses = warehouseService.getWarehousesByStatus(status);
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/city/{city}")
    @Operation(
            summary = "Get warehouses by city",
            description = "Retrieve all warehouses located in a specific city. Useful for regional inventory management and order fulfillment."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouses in the specified city retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = WarehouseResponseDTO.class))
            )
    )
    public ResponseEntity<List<WarehouseResponseDTO>> getWarehousesByCity(
            @Parameter(description = "City name", example = "New York", required = true)
            @PathVariable String city) {
        List<WarehouseResponseDTO> warehouses = warehouseService.getWarehousesByCity(city);
        return ResponseEntity.ok(warehouses);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update warehouse details",
            description = "Update warehouse information including name, address, capacity, contact details, and operational parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Warehouse updated successfully",
                    content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Warehouse not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Warehouse code already exists"
            )
    })
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(
            @Parameter(description = "Warehouse ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated warehouse details", required = true)
            @Valid @RequestBody WarehouseUpdateDTO updateDTO) {
        WarehouseResponseDTO responseDTO = warehouseService.updateWarehouse(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update warehouse status",
            description = "Change warehouse operational status (e.g., ACTIVE, INACTIVE, MAINTENANCE, CLOSED). Status changes may affect order fulfillment routing."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Warehouse status updated successfully",
                    content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status value"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Warehouse not found with given ID"
            )
    })
    public ResponseEntity<WarehouseResponseDTO> updateWarehouseStatus(
            @Parameter(description = "Warehouse ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New warehouse status", example = "ACTIVE", required = true)
            @RequestParam WarehouseStatus status) {
        WarehouseResponseDTO responseDTO = warehouseService.updateWarehouseStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a warehouse",
            description = "Permanently delete a warehouse. Only warehouses with no inventory can be deleted. (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Warehouse deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Warehouse not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Warehouse cannot be deleted (has inventory or active operations)"
            )
    })
    public ResponseEntity<Void> deleteWarehouse(
            @Parameter(description = "Warehouse ID", example = "1", required = true)
            @PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}


/**

package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.WarehouseRequestDTO;
import com.ecommerce.inventory.dto.WarehouseResponseDTO;
import com.ecommerce.inventory.dto.WarehouseUpdateDTO;
import com.ecommerce.inventory.entity.WarehouseStatus;
import com.ecommerce.inventory.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody WarehouseRequestDTO requestDTO) {
        WarehouseResponseDTO responseDTO = warehouseService.createWarehouse(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseById(@PathVariable Long id) {
        WarehouseResponseDTO responseDTO = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseByCode(@PathVariable String code) {
        WarehouseResponseDTO responseDTO = warehouseService.getWarehouseByCode(code);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponseDTO>> getAllWarehouses() {
        List<WarehouseResponseDTO> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WarehouseResponseDTO>> getWarehousesByStatus(@PathVariable WarehouseStatus status) {
        List<WarehouseResponseDTO> warehouses = warehouseService.getWarehousesByStatus(status);
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<WarehouseResponseDTO>> getWarehousesByCity(@PathVariable String city) {
        List<WarehouseResponseDTO> warehouses = warehouseService.getWarehousesByCity(city);
        return ResponseEntity.ok(warehouses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseUpdateDTO updateDTO) {
        WarehouseResponseDTO responseDTO = warehouseService.updateWarehouse(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouseStatus(
            @PathVariable Long id,
            @RequestParam WarehouseStatus status) {
        WarehouseResponseDTO responseDTO = warehouseService.updateWarehouseStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}

 */