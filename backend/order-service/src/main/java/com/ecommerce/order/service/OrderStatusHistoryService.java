package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderStatusHistoryResponseDTO;
import com.ecommerce.order.entity.OrderStatusHistory;
import com.ecommerce.order.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public List<OrderStatusHistoryResponseDTO> getHistoryByOrderId(Long orderId) {
        return orderStatusHistoryRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderStatusHistoryResponseDTO> getAllHistory() {
        return orderStatusHistoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private OrderStatusHistoryResponseDTO mapToResponseDTO(OrderStatusHistory history) {
        OrderStatusHistoryResponseDTO dto = new OrderStatusHistoryResponseDTO();
        dto.setId(history.getId());
        dto.setOrderId(history.getOrderId());
        dto.setPreviousStatus(history.getPreviousStatus());
        dto.setNewStatus(history.getNewStatus());
        dto.setRemarks(history.getRemarks());
        dto.setChangedBy(history.getChangedBy());
        dto.setCreatedAt(history.getCreatedAt());
        return dto;
    }
}