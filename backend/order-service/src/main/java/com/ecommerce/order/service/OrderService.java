package com.ecommerce.order.service;

import com.ecommerce.order.client.InventoryClient;
import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.*;
import com.ecommerce.order.exception.*;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        // 1. Validate user exists
        UserResponseDTO user = userClient.getUserById(requestDTO.getUserId());

        // 2. Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(requestDTO.getUserId());
        order.setCustomerName(requestDTO.getCustomerName());
        order.setCustomerEmail(requestDTO.getCustomerEmail());
        order.setCustomerPhone(requestDTO.getCustomerPhone());

        // 3. Set shipping address
        order.setShippingAddress(requestDTO.getShippingAddress());
        order.setShippingCity(requestDTO.getShippingCity());
        order.setShippingState(requestDTO.getShippingState());
        order.setShippingCountry(requestDTO.getShippingCountry());
        order.setShippingZipCode(requestDTO.getShippingZipCode());

        // 4. Set billing address (default to shipping if not provided)
        if (requestDTO.getBillingAddress() != null) {
            order.setBillingAddress(requestDTO.getBillingAddress());
            order.setBillingCity(requestDTO.getBillingCity());
            order.setBillingState(requestDTO.getBillingState());
            order.setBillingCountry(requestDTO.getBillingCountry());
            order.setBillingZipCode(requestDTO.getBillingZipCode());
        } else {
            order.setBillingAddress(requestDTO.getShippingAddress());
            order.setBillingCity(requestDTO.getShippingCity());
            order.setBillingState(requestDTO.getShippingState());
            order.setBillingCountry(requestDTO.getShippingCountry());
            order.setBillingZipCode(requestDTO.getShippingZipCode());
        }

        order.setPaymentMethod(requestDTO.getPaymentMethod());
        order.setNotes(requestDTO.getNotes());

        // 5. Process order items
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : requestDTO.getOrderItems()) {
            // Validate product exists
            ProductResponseDTO product = productClient.getProductById(itemDTO.getProductId());

            // Check stock availability
            Boolean stockAvailable = inventoryClient.checkStockAvailability(
                    itemDTO.getProductId(), itemDTO.getQuantity());

            if (!stockAvailable) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setSku(product.getSku());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setDiscountPrice(product.getDiscountPrice());

            // Calculate item total (use discount price if available)
            BigDecimal itemPrice = product.getDiscountPrice() != null ?
                    product.getDiscountPrice() : product.getPrice();
            BigDecimal itemTotal = itemPrice.multiply(new BigDecimal(itemDTO.getQuantity()));
            orderItem.setTotalPrice(itemTotal);
            orderItem.setProductImageUrl(product.getImageUrl());

            order.addOrderItem(orderItem);
            subtotal = subtotal.add(itemTotal);
        }

        // 6. Calculate totals
        order.setSubtotal(subtotal);

        BigDecimal discountAmount = requestDTO.getDiscountAmount() != null ?
                requestDTO.getDiscountAmount() : BigDecimal.ZERO;
        order.setDiscountAmount(discountAmount);

        BigDecimal shippingFee = requestDTO.getShippingFee() != null ?
                requestDTO.getShippingFee() : BigDecimal.ZERO;
        order.setShippingFee(shippingFee);

        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = taxableAmount.multiply(TAX_RATE);
        order.setTaxAmount(taxAmount);

        BigDecimal totalAmount = subtotal.subtract(discountAmount).add(taxAmount).add(shippingFee);
        order.setTotalAmount(totalAmount);

        // 7. Save order
        Order savedOrder = orderRepository.save(order);

        // 8. Reserve stock for all items
        try {
            for (OrderItem item : savedOrder.getOrderItems()) {
                inventoryClient.reserveStock(
                        item.getProductId(),
                        item.getQuantity(),
                        savedOrder.getOrderNumber()
                );
            }
        } catch (Exception e) {
            log.error("Failed to reserve stock for order: {}", savedOrder.getOrderNumber(), e);
            throw new InsufficientStockException("Failed to reserve stock: " + e.getMessage());
        }

        // 9. Create status history
        createStatusHistory(savedOrder.getId(), null, OrderStatus.PENDING, "Order created", null);

        log.info("Order created successfully: {}", savedOrder.getOrderNumber());

        return mapToResponseDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponseDTO(order);
    }

    public OrderResponseDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
        return mapToResponseDTO(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByUserAndStatus(Long userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusUpdateDTO updateDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        OrderStatus previousStatus = order.getStatus();
        OrderStatus newStatus = updateDTO.getStatus();

        // Validate status transition
        validateStatusTransition(previousStatus, newStatus);

        // Update status
        order.setStatus(newStatus);

        // Update timestamps based on status
        switch (newStatus) {
            case CONFIRMED:
                order.setConfirmedAt(LocalDateTime.now());
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                // Confirm stock reservation
                confirmStockReservation(order);
                break;
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                break;
            case CANCELLED:
                order.setCancelledAt(LocalDateTime.now());
                // Release reserved stock
                releaseStockReservation(order);
                break;
            default:
                break;
        }

        Order updatedOrder = orderRepository.save(order);

        // Create status history
        createStatusHistory(id, previousStatus, newStatus, updateDTO.getRemarks(), updateDTO.getChangedBy());

        log.info("Order status updated: {} from {} to {}", order.getOrderNumber(), previousStatus, newStatus);

        return mapToResponseDTO(updatedOrder);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long id, OrderCancellationDTO cancellationDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // Only PENDING and CONFIRMED orders can be cancelled
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException("Order cannot be cancelled in " + order.getStatus() + " status");
        }

        OrderStatus previousStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(cancellationDTO.getReason());
        order.setPaymentStatus(PaymentStatus.REFUNDED);

        // Release reserved stock
        releaseStockReservation(order);

        Order cancelledOrder = orderRepository.save(order);

        // Create status history
        createStatusHistory(id, previousStatus, OrderStatus.CANCELLED,
                cancellationDTO.getReason(), cancellationDTO.getCancelledBy());

        log.info("Order cancelled: {}", order.getOrderNumber());

        return mapToResponseDTO(cancelledOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // Only CANCELLED orders can be deleted
        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Only cancelled orders can be deleted");
        }

        orderRepository.delete(order);
        log.info("Order deleted: {}", order.getOrderNumber());
    }

    private void confirmStockReservation(Order order) {
        try {
            for (OrderItem item : order.getOrderItems()) {
                inventoryClient.confirmReservation(
                        item.getProductId(),
                        item.getQuantity(),
                        order.getOrderNumber()
                );
            }
            log.info("Stock reservation confirmed for order: {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to confirm stock reservation for order: {}", order.getOrderNumber(), e);
            throw new RuntimeException("Failed to confirm stock reservation: " + e.getMessage());
        }
    }

    private void releaseStockReservation(Order order) {
        try {
            for (OrderItem item : order.getOrderItems()) {
                inventoryClient.releaseReservedStock(
                        item.getProductId(),
                        item.getQuantity(),
                        order.getOrderNumber()
                );
            }
            log.info("Stock reservation released for order: {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to release stock reservation for order: {}", order.getOrderNumber(), e);
            // Don't throw exception here, just log it
        }
    }

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        // Define valid transitions
        boolean isValidTransition = switch (from) {
            case PENDING -> to == OrderStatus.CONFIRMED || to == OrderStatus.CANCELLED;
            case CONFIRMED -> to == OrderStatus.PROCESSING || to == OrderStatus.CANCELLED;
            case PROCESSING -> to == OrderStatus.SHIPPED || to == OrderStatus.CANCELLED;
            case SHIPPED -> to == OrderStatus.DELIVERED || to == OrderStatus.RETURNED;
            case DELIVERED -> to == OrderStatus.RETURNED;
            case CANCELLED, RETURNED, REFUNDED -> false; // Terminal states
        };

        if (!isValidTransition) {
            throw new InvalidOrderStateException(
                    "Invalid status transition from " + from + " to " + to);
        }
    }

    private void createStatusHistory(Long orderId, OrderStatus previousStatus,
                                     OrderStatus newStatus, String remarks, Long changedBy) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrderId(orderId);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setRemarks(remarks);
        history.setChangedBy(changedBy);

        orderStatusHistoryRepository.save(history);
    }

    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderRepository.count() + 1;
        String sequencePart = String.format("%05d", count);
        return "ORD-" + datePart + "-" + sequencePart;
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserId(order.getUserId());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setCustomerPhone(order.getCustomerPhone());

        // Map order items
        dto.setOrderItems(order.getOrderItems().stream()
                .map(this::mapOrderItemToDTO)
                .collect(Collectors.toList()));

        dto.setSubtotal(order.getSubtotal());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setShippingFee(order.getShippingFee());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentTransactionId(order.getPaymentTransactionId());

        // Shipping address
        dto.setShippingAddress(order.getShippingAddress());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingState(order.getShippingState());
        dto.setShippingCountry(order.getShippingCountry());
        dto.setShippingZipCode(order.getShippingZipCode());

        // Billing address
        dto.setBillingAddress(order.getBillingAddress());
        dto.setBillingCity(order.getBillingCity());
        dto.setBillingState(order.getBillingState());
        dto.setBillingCountry(order.getBillingCountry());
        dto.setBillingZipCode(order.getBillingZipCode());

        dto.setNotes(order.getNotes());
        dto.setConfirmedAt(order.getConfirmedAt());
        dto.setShippedAt(order.getShippedAt());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setCancelledAt(order.getCancelledAt());
        dto.setCancellationReason(order.getCancellationReason());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }

    private OrderItemResponseDTO mapOrderItemToDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setSku(item.getSku());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setDiscountPrice(item.getDiscountPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setProductImageUrl(item.getProductImageUrl());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }
}