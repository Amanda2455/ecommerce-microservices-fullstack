package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.RefundRequestDTO;
import com.ecommerce.payment.dto.RefundResponseDTO;
import com.ecommerce.payment.entity.*;
import com.ecommerce.payment.exception.InsufficientRefundAmountException;
import com.ecommerce.payment.exception.InvalidPaymentStateException;
import com.ecommerce.payment.exception.ResourceNotFoundException;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.repository.PaymentTransactionRepository;
import com.ecommerce.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final Random random = new Random();

    @Transactional
    public RefundResponseDTO createRefund(RefundRequestDTO requestDTO) {
        // 1. Validate payment exists
        Payment payment = paymentRepository.findById(requestDTO.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + requestDTO.getPaymentId()));

        // 2. Validate payment is completed
        if (payment.getStatus() != PaymentStatus.COMPLETED &&
                payment.getStatus() != PaymentStatus.PARTIALLY_REFUNDED) {
            throw new InvalidPaymentStateException("Only completed or partially refunded payments can be refunded");
        }

        // 3. Calculate already refunded amount
        BigDecimal alreadyRefunded = refundRepository.findByPaymentId(requestDTO.getPaymentId()).stream()
                .filter(r -> r.getStatus() == RefundStatus.COMPLETED)
                .map(Refund::getRefundedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Validate refund amount
        BigDecimal availableForRefund = payment.getAmount().subtract(alreadyRefunded);
        if (requestDTO.getAmount().compareTo(availableForRefund) > 0) {
            throw new InsufficientRefundAmountException("Refund amount exceeds available amount. Available: " + availableForRefund);
        }

        // 5. Create refund
        Refund refund = new Refund();
        refund.setRefundId(generateRefundId());
        refund.setPaymentId(requestDTO.getPaymentId());
        refund.setOrderId(payment.getOrderId());
        refund.setAmount(requestDTO.getAmount());
        refund.setRefundedAmount(BigDecimal.ZERO);
        refund.setReason(requestDTO.getReason());
        refund.setRemarks(requestDTO.getRemarks());
        refund.setInitiatedBy(requestDTO.getInitiatedBy());

        Refund savedRefund = refundRepository.save(refund);

        log.info("Refund created: {}", savedRefund.getRefundId());

        return mapToResponseDTO(savedRefund);
    }

    @Transactional
    public RefundResponseDTO processRefund(Long id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with id: " + id));

        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new InvalidPaymentStateException("Refund is not in PENDING status");
        }

        Payment payment = paymentRepository.findById(refund.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        refund.setStatus(RefundStatus.PROCESSING);
        refundRepository.save(refund);

        // Simulate refund processing (95% success rate)
        boolean success = random.nextInt(100) < 95;

        if (success) {
            refund.setStatus(RefundStatus.COMPLETED);
            refund.setRefundedAmount(refund.getAmount());
            refund.setGatewayRefundId("REF-GW-" + UUID.randomUUID().toString());
            refund.setProcessedAt(LocalDateTime.now());

            // Update payment status
            BigDecimal totalRefunded = refundRepository.findByPaymentId(payment.getId()).stream()
                    .filter(r -> r.getStatus() == RefundStatus.COMPLETED)
                    .map(Refund::getRefundedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalRefunded = totalRefunded.add(refund.getRefundedAmount());

            if (totalRefunded.compareTo(payment.getAmount()) >= 0) {
                payment.setStatus(PaymentStatus.REFUNDED);
                payment.setRefundedAt(LocalDateTime.now());
            } else {
                payment.setStatus(PaymentStatus.PARTIALLY_REFUNDED);
            }

            // Create refund transaction
            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setTransactionId(generateTransactionId());
            transaction.setTransactionType(TransactionType.REFUND);
            transaction.setAmount(refund.getAmount());
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setGatewayTransactionId(refund.getGatewayRefundId());
            transaction.setRemarks("Refund processed successfully");
            payment.addTransaction(transaction);

            paymentRepository.save(payment);

            log.info("Refund processed successfully: {}", refund.getRefundId());

        } else {
            refund.setStatus(RefundStatus.FAILED);
            refund.setRemarks("Gateway declined the refund");

            log.warn("Refund processing failed: {}", refund.getRefundId());
        }

        Refund processedRefund = refundRepository.save(refund);

        return mapToResponseDTO(processedRefund);
    }

    public RefundResponseDTO getRefundById(Long id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with id: " + id));
        return mapToResponseDTO(refund);
    }

    public RefundResponseDTO getRefundByRefundId(String refundId) {
        Refund refund = refundRepository.findByRefundId(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with refund ID: " + refundId));
        return mapToResponseDTO(refund);
    }

    public List<RefundResponseDTO> getRefundsByPaymentId(Long paymentId) {
        return refundRepository.findByPaymentId(paymentId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RefundResponseDTO> getRefundsByOrderId(Long orderId) {
        return refundRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RefundResponseDTO> getRefundsByStatus(RefundStatus status) {
        return refundRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RefundResponseDTO> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelRefund(Long id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with id: " + id));

        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new InvalidPaymentStateException("Only pending refunds can be cancelled");
        }

        refund.setStatus(RefundStatus.CANCELLED);
        refund.setRemarks(refund.getRemarks() + " - Cancelled by user");

        refundRepository.save(refund);

        log.info("Refund cancelled: {}", refund.getRefundId());
    }

    private String generateRefundId() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = refundRepository.count() + 1;
        String sequencePart = String.format("%05d", count);
        return "REF-" + datePart + "-" + sequencePart;
    }

    private String generateTransactionId() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = String.format("%04d", random.nextInt(10000));
        return "TXN-" + datePart + "-" + randomPart;
    }

    private RefundResponseDTO mapToResponseDTO(Refund refund) {
        RefundResponseDTO dto = new RefundResponseDTO();
        dto.setId(refund.getId());
        dto.setRefundId(refund.getRefundId());
        dto.setPaymentId(refund.getPaymentId());
        dto.setOrderId(refund.getOrderId());
        dto.setAmount(refund.getAmount());
        dto.setRefundedAmount(refund.getRefundedAmount());
        dto.setStatus(refund.getStatus());
        dto.setReason(refund.getReason());
        dto.setGatewayRefundId(refund.getGatewayRefundId());
        dto.setRemarks(refund.getRemarks());
        dto.setInitiatedBy(refund.getInitiatedBy());
        dto.setProcessedAt(refund.getProcessedAt());
        dto.setCreatedAt(refund.getCreatedAt());
        dto.setUpdatedAt(refund.getUpdatedAt());
        return dto;
    }
}