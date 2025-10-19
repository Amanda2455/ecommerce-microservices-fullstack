package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.StockMovementResponseDTO;
import com.ecommerce.inventory.entity.MovementType;
import com.ecommerce.inventory.entity.StockMovement;
import com.ecommerce.inventory.exception.ResourceNotFoundException;
import com.ecommerce.inventory.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public StockMovementResponseDTO getMovementById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        return mapToResponseDTO(movement);
    }

    public List<StockMovementResponseDTO> getAllMovements() {
        return stockMovementRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponseDTO> getMovementsByInventory(Long inventoryId) {
        return stockMovementRepository.findByInventoryIdOrderByCreatedAtDesc(inventoryId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponseDTO> getMovementsByType(MovementType movementType) {
        return stockMovementRepository.findByMovementType(movementType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponseDTO> getMovementsByReference(String referenceId) {
        return stockMovementRepository.findByReferenceId(referenceId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponseDTO> getMovementsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return stockMovementRepository.findByDateRange(startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private StockMovementResponseDTO mapToResponseDTO(StockMovement movement) {
        StockMovementResponseDTO dto = new StockMovementResponseDTO();
        dto.setId(movement.getId());
        dto.setInventoryId(movement.getInventory().getId());
        dto.setProductName(movement.getInventory().getProductName());
        dto.setSku(movement.getInventory().getSku());
        dto.setMovementType(movement.getMovementType());
        dto.setQuantity(movement.getQuantity());
        dto.setPreviousQuantity(movement.getPreviousQuantity());
        dto.setNewQuantity(movement.getNewQuantity());
        dto.setReferenceId(movement.getReferenceId());
        dto.setReason(movement.getReason());
        dto.setNotes(movement.getNotes());
        dto.setPerformedBy(movement.getPerformedBy());
        dto.setCreatedAt(movement.getCreatedAt());
        return dto;
    }
}