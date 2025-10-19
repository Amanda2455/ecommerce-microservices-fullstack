package com.ecommerce.inventory.service;

import com.ecommerce.inventory.client.ProductClient;
import com.ecommerce.inventory.dto.*;
import com.ecommerce.inventory.entity.*;
import com.ecommerce.inventory.exception.InsufficientStockException;
import com.ecommerce.inventory.exception.InventoryAlreadyExistsException;
import com.ecommerce.inventory.exception.ResourceNotFoundException;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.repository.StockMovementRepository;
import com.ecommerce.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductClient productClient;

    @Transactional
    public InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO) {
        // Check if inventory already exists for this product
        if (inventoryRepository.existsByProductId(requestDTO.getProductId())) {
            throw new InventoryAlreadyExistsException("Inventory already exists for product ID: " + requestDTO.getProductId());
        }

        // Validate warehouse
        Warehouse warehouse = warehouseRepository.findById(requestDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + requestDTO.getWarehouseId()));

        // Fetch product details from Product Service
        ProductResponseDTO product = productClient.getProductById(requestDTO.getProductId());

        Inventory inventory = new Inventory();
        inventory.setProductId(requestDTO.getProductId());
        inventory.setProductName(requestDTO.getProductName());
        inventory.setSku(requestDTO.getSku());
        inventory.setAvailableQuantity(requestDTO.getAvailableQuantity());
        inventory.setReservedQuantity(0);
        inventory.setTotalQuantity(requestDTO.getAvailableQuantity());
        inventory.setReorderLevel(requestDTO.getReorderLevel() != null ? requestDTO.getReorderLevel() : 10);
        inventory.setReorderQuantity(requestDTO.getReorderQuantity() != null ? requestDTO.getReorderQuantity() : 50);
        inventory.setWarehouse(warehouse);
        inventory.setLastRestockedAt(LocalDateTime.now());

        // Set status based on quantity
        inventory.setStatus(determineStatus(inventory));

        Inventory savedInventory = inventoryRepository.save(inventory);

        // Create stock movement record
        createStockMovement(savedInventory, MovementType.IN, requestDTO.getAvailableQuantity(),
                0, requestDTO.getAvailableQuantity(), null, MovementReason.PURCHASE, "Initial stock", null);

        /***
         * ✅ NO SYNC - Inventory Service is source of truth
         * Update product stock in Product Service
         * productClient.updateProductStock(requestDTO.getProductId(), requestDTO.getAvailableQuantity());
         * * * */

        log.info("Inventory created for product ID: {}", requestDTO.getProductId());

        return mapToResponseDTO(savedInventory);
    }

    public InventoryResponseDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
        return mapToResponseDTO(inventory);
    }

    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));
        return mapToResponseDTO(inventory);
    }

    public InventoryResponseDTO getInventoryBySku(String sku) {
        Inventory inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for SKU: " + sku));
        return mapToResponseDTO(inventory);
    }

    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryResponseDTO> getInventoryByWarehouse(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryResponseDTO> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryResponseDTO> getOutOfStockItems() {
        return inventoryRepository.findOutOfStockItems().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryResponseDTO> searchInventory(String keyword) {
        return inventoryRepository.searchInventory(keyword).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryResponseDTO updateInventory(Long id, InventoryUpdateDTO updateDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        if (updateDTO.getAvailableQuantity() != null) {
            int oldQuantity = inventory.getAvailableQuantity();
            inventory.setAvailableQuantity(updateDTO.getAvailableQuantity());
            inventory.setTotalQuantity(updateDTO.getAvailableQuantity() + inventory.getReservedQuantity());

            // Create stock movement
            MovementType movementType = updateDTO.getAvailableQuantity() > oldQuantity ? MovementType.IN : MovementType.OUT;
            int quantityChange = Math.abs(updateDTO.getAvailableQuantity() - oldQuantity);
            createStockMovement(inventory, movementType, quantityChange, oldQuantity,
                    updateDTO.getAvailableQuantity(), null, MovementReason.ADJUSTMENT, "Manual adjustment", null);
        }

        if (updateDTO.getReorderLevel() != null) {
            inventory.setReorderLevel(updateDTO.getReorderLevel());
        }

        if (updateDTO.getReorderQuantity() != null) {
            inventory.setReorderQuantity(updateDTO.getReorderQuantity());
        }

        if (updateDTO.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(updateDTO.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + updateDTO.getWarehouseId()));
            inventory.setWarehouse(warehouse);
        }

        inventory.setStatus(determineStatus(inventory));
        Inventory updatedInventory = inventoryRepository.save(inventory);

        log.info("Inventory updated for ID: {}", id);

        return mapToResponseDTO(updatedInventory);
    }

    @Transactional
    public InventoryResponseDTO addStock(Long id, StockAdjustmentDTO adjustmentDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        int previousQuantity = inventory.getAvailableQuantity();
        int newQuantity = previousQuantity + adjustmentDTO.getQuantity();

        inventory.setAvailableQuantity(newQuantity);
        inventory.setTotalQuantity(newQuantity + inventory.getReservedQuantity());
        inventory.setLastRestockedAt(LocalDateTime.now());
        inventory.setStatus(determineStatus(inventory));

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Create stock movement
        createStockMovement(updatedInventory, MovementType.IN, adjustmentDTO.getQuantity(),
                previousQuantity, newQuantity, adjustmentDTO.getReferenceId(),
                adjustmentDTO.getReason(), adjustmentDTO.getNotes(), adjustmentDTO.getPerformedBy());

        /***
         * ✅ NO SYNC - Removed
         * Update product stock
         * productClient.updateProductStock(inventory.getProductId(), adjustmentDTO.getQuantity());
        **/

        log.info("Stock added to inventory ID: {}, Quantity: {}", id, adjustmentDTO.getQuantity());

        return mapToResponseDTO(updatedInventory);
    }

    @Transactional
    public InventoryResponseDTO removeStock(Long id, StockAdjustmentDTO adjustmentDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        if (inventory.getAvailableQuantity() < adjustmentDTO.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock. Available: " +
                    inventory.getAvailableQuantity() + ", Requested: " + adjustmentDTO.getQuantity());
        }

        int previousQuantity = inventory.getAvailableQuantity();
        int newQuantity = previousQuantity - adjustmentDTO.getQuantity();

        inventory.setAvailableQuantity(newQuantity);
        inventory.setTotalQuantity(newQuantity + inventory.getReservedQuantity());
        inventory.setStatus(determineStatus(inventory));

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Create stock movement
        createStockMovement(updatedInventory, MovementType.OUT, adjustmentDTO.getQuantity(),
                previousQuantity, newQuantity, adjustmentDTO.getReferenceId(),
                adjustmentDTO.getReason(), adjustmentDTO.getNotes(), adjustmentDTO.getPerformedBy());

        /***
         * ✅ NO SYNC - Removed
         * Update product stock
         * productClient.updateProductStock(inventory.getProductId(), -adjustmentDTO.getQuantity());
        **/

        log.info("Stock removed from inventory ID: {}, Quantity: {}", id, adjustmentDTO.getQuantity());

        return mapToResponseDTO(updatedInventory);
    }

    @Transactional
    public InventoryResponseDTO reserveStock(Long productId, Integer quantity, String orderId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + inventory.getProductName() +
                    ". Available: " + inventory.getAvailableQuantity() + ", Requested: " + quantity);
        }

        int previousAvailable = inventory.getAvailableQuantity();
        inventory.setAvailableQuantity(previousAvailable - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventory.setStatus(determineStatus(inventory));

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Create stock movement
        createStockMovement(updatedInventory, MovementType.RESERVED, quantity,
                previousAvailable, inventory.getAvailableQuantity(), orderId,
                MovementReason.ORDER_RESERVATION, "Stock reserved for order", null);

        log.info("Stock reserved for product ID: {}, Quantity: {}, Order: {}", productId, quantity, orderId);

        return mapToResponseDTO(updatedInventory);
    }

    @Transactional
    public InventoryResponseDTO releaseReservedStock(Long productId, Integer quantity, String orderId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));

        if (inventory.getReservedQuantity() < quantity) {
            throw new IllegalStateException("Cannot release more stock than reserved. Reserved: " +
                    inventory.getReservedQuantity() + ", Requested: " + quantity);
        }

        int previousAvailable = inventory.getAvailableQuantity();
        inventory.setAvailableQuantity(previousAvailable + quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setStatus(determineStatus(inventory));

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Create stock movement
        createStockMovement(updatedInventory, MovementType.RELEASED, quantity,
                previousAvailable, inventory.getAvailableQuantity(), orderId,
                MovementReason.ORDER_CANCELLATION, "Reserved stock released", null);

        log.info("Reserved stock released for product ID: {}, Quantity: {}, Order: {}", productId, quantity, orderId);

        return mapToResponseDTO(updatedInventory);
    }

    @Transactional
    public InventoryResponseDTO confirmReservation(Long productId, Integer quantity, String orderId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));

        if (inventory.getReservedQuantity() < quantity) {
            throw new IllegalStateException("Cannot confirm more stock than reserved. Reserved: " +
                    inventory.getReservedQuantity() + ", Requested: " + quantity);
        }

        int previousReserved = inventory.getReservedQuantity();
        inventory.setReservedQuantity(previousReserved - quantity);
        inventory.setTotalQuantity(inventory.getAvailableQuantity() + inventory.getReservedQuantity());
        inventory.setStatus(determineStatus(inventory));

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Create stock movement
        createStockMovement(updatedInventory, MovementType.OUT, quantity,
                previousReserved, inventory.getReservedQuantity(), orderId,
                MovementReason.SALE, "Order confirmed - stock sold", null);

        log.info("Reservation confirmed for product ID: {}, Quantity: {}, Order: {}", productId, quantity, orderId);

        return mapToResponseDTO(updatedInventory);
    }

    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        inventoryRepository.delete(inventory);
        log.info("Inventory deleted - ID: {}", id);
    }

    public boolean checkStockAvailability(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));

        return inventory.getAvailableQuantity() >= quantity;
    }

    private void createStockMovement(Inventory inventory, MovementType movementType,
                                     Integer quantity, Integer previousQuantity, Integer newQuantity,
                                     String referenceId, MovementReason reason, String notes, Long performedBy) {
        StockMovement movement = new StockMovement();
        movement.setInventory(inventory);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setPreviousQuantity(previousQuantity);
        movement.setNewQuantity(newQuantity);
        movement.setReferenceId(referenceId);
        movement.setReason(reason);
        movement.setNotes(notes);
        movement.setPerformedBy(performedBy);

        stockMovementRepository.save(movement);
    }

    private InventoryStatus determineStatus(Inventory inventory) {
        if (inventory.getAvailableQuantity() == 0) {
            return InventoryStatus.OUT_OF_STOCK;
        } else if (inventory.getAvailableQuantity() <= inventory.getReorderLevel()) {
            return InventoryStatus.LOW_STOCK;
        } else {
            return InventoryStatus.IN_STOCK;
        }
    }

    private InventoryResponseDTO mapToResponseDTO(Inventory inventory) {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setProductName(inventory.getProductName());
        dto.setSku(inventory.getSku());
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());
        dto.setTotalQuantity(inventory.getTotalQuantity());
        dto.setReorderLevel(inventory.getReorderLevel());
        dto.setReorderQuantity(inventory.getReorderQuantity());
        dto.setWarehouse(mapWarehouseToSimpleDTO(inventory.getWarehouse()));
        dto.setStatus(inventory.getStatus());
        dto.setCreatedAt(inventory.getCreatedAt());
        dto.setUpdatedAt(inventory.getUpdatedAt());
        dto.setLastRestockedAt(inventory.getLastRestockedAt());
        return dto;
    }

    private WarehouseResponseDTO mapWarehouseToSimpleDTO(Warehouse warehouse) {
        if (warehouse == null) return null;

        WarehouseResponseDTO dto = new WarehouseResponseDTO();
        dto.setId(warehouse.getId());
        dto.setCode(warehouse.getCode());
        dto.setName(warehouse.getName());
        dto.setCity(warehouse.getCity());
        return dto;
    }
}