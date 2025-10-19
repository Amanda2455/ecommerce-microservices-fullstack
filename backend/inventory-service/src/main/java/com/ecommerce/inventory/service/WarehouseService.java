package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.WarehouseRequestDTO;
import com.ecommerce.inventory.dto.WarehouseResponseDTO;
import com.ecommerce.inventory.dto.WarehouseUpdateDTO;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.entity.WarehouseStatus;
import com.ecommerce.inventory.exception.ResourceNotFoundException;
import com.ecommerce.inventory.exception.WarehouseAlreadyExistsException;
import com.ecommerce.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Transactional
    public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO requestDTO) {
        // Check if warehouse with code already exists
        if (warehouseRepository.existsByCode(requestDTO.getCode())) {
            throw new WarehouseAlreadyExistsException("Warehouse with code " + requestDTO.getCode() + " already exists");
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setCode(requestDTO.getCode());
        warehouse.setName(requestDTO.getName());
        warehouse.setAddress(requestDTO.getAddress());
        warehouse.setCity(requestDTO.getCity());
        warehouse.setState(requestDTO.getState());
        warehouse.setCountry(requestDTO.getCountry());
        warehouse.setZipCode(requestDTO.getZipCode());
        warehouse.setContactPerson(requestDTO.getContactPerson());
        warehouse.setContactPhone(requestDTO.getContactPhone());
        warehouse.setContactEmail(requestDTO.getContactEmail());

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        log.info("Warehouse created successfully with code: {}", savedWarehouse.getCode());

        return mapToResponseDTO(savedWarehouse);
    }

    public WarehouseResponseDTO getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        return mapToResponseDTO(warehouse);
    }

    public WarehouseResponseDTO getWarehouseByCode(String code) {
        Warehouse warehouse = warehouseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with code: " + code));
        return mapToResponseDTO(warehouse);
    }

    public List<WarehouseResponseDTO> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WarehouseResponseDTO> getWarehousesByStatus(WarehouseStatus status) {
        return warehouseRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WarehouseResponseDTO> getWarehousesByCity(String city) {
        return warehouseRepository.findByCity(city).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WarehouseResponseDTO updateWarehouse(Long id, WarehouseUpdateDTO updateDTO) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        if (updateDTO.getName() != null) {
            warehouse.setName(updateDTO.getName());
        }
        if (updateDTO.getAddress() != null) {
            warehouse.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getCity() != null) {
            warehouse.setCity(updateDTO.getCity());
        }
        if (updateDTO.getState() != null) {
            warehouse.setState(updateDTO.getState());
        }
        if (updateDTO.getCountry() != null) {
            warehouse.setCountry(updateDTO.getCountry());
        }
        if (updateDTO.getZipCode() != null) {
            warehouse.setZipCode(updateDTO.getZipCode());
        }
        if (updateDTO.getContactPerson() != null) {
            warehouse.setContactPerson(updateDTO.getContactPerson());
        }
        if (updateDTO.getContactPhone() != null) {
            warehouse.setContactPhone(updateDTO.getContactPhone());
        }
        if (updateDTO.getContactEmail() != null) {
            warehouse.setContactEmail(updateDTO.getContactEmail());
        }

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        log.info("Warehouse updated successfully with ID: {}", id);

        return mapToResponseDTO(updatedWarehouse);
    }

    @Transactional
    public WarehouseResponseDTO updateWarehouseStatus(Long id, WarehouseStatus status) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        warehouse.setStatus(status);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        return mapToResponseDTO(updatedWarehouse);
    }

    @Transactional
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        if (!warehouse.getInventories().isEmpty()) {
            throw new IllegalStateException("Cannot delete warehouse with existing inventory. Please move inventory first.");
        }

        warehouseRepository.delete(warehouse);
        log.info("Warehouse deleted - ID: {}", id);
    }

    private WarehouseResponseDTO mapToResponseDTO(Warehouse warehouse) {
        WarehouseResponseDTO dto = new WarehouseResponseDTO();
        dto.setId(warehouse.getId());
        dto.setCode(warehouse.getCode());
        dto.setName(warehouse.getName());
        dto.setAddress(warehouse.getAddress());
        dto.setCity(warehouse.getCity());
        dto.setState(warehouse.getState());
        dto.setCountry(warehouse.getCountry());
        dto.setZipCode(warehouse.getZipCode());
        dto.setContactPerson(warehouse.getContactPerson());
        dto.setContactPhone(warehouse.getContactPhone());
        dto.setContactEmail(warehouse.getContactEmail());
        dto.setStatus(warehouse.getStatus());
        dto.setInventoryCount(warehouse.getInventories() != null ? warehouse.getInventories().size() : 0);
        dto.setCreatedAt(warehouse.getCreatedAt());
        dto.setUpdatedAt(warehouse.getUpdatedAt());
        return dto;
    }
}