package com.ecommerce.payment.service;

import com.ecommerce.payment.client.OrderClient;
import com.ecommerce.payment.dto.*;
import com.ecommerce.payment.entity.*;
import com.ecommerce.payment.exception.*;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.repository.PaymentTransactionRepository;
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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final OrderClient orderClient;
    private final Random random = new Random();

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {
        // 1. Validate order exists
        OrderResponseDTO order = orderClient.getOrderById(requestDTO.getOrderId());

        // 2. Check if payment already exists for this order
        if (paymentRepository.existsByOrderId(requestDTO.getOrderId())) {
            throw new PaymentAlreadyExistsException("Payment already exists for order ID: " + requestDTO.getOrderId());
        }

        // 3. Create payment
        Payment payment = new Payment();
        payment.setPaymentId(generatePaymentId());
        payment.setOrderId(requestDTO.getOrderId());
        payment.setOrderNumber(requestDTO.getOrderNumber());
        payment.setUserId(requestDTO.getUserId());
        payment.setAmount(requestDTO.getAmount());
        payment.setCurrency(requestDTO.getCurrency() != null ? requestDTO.getCurrency() : "USD");
        payment.setPaymentMethod(requestDTO.getPaymentMethod());
        payment.setCustomerEmail(requestDTO.getCustomerEmail());
        payment.setCustomerPhone(requestDTO.getCustomerPhone());
        payment.setDescription(requestDTO.getDescription());

        // 4. Set payment gateway based on method
        payment.setPaymentGateway(determinePaymentGateway(requestDTO.getPaymentMethod()));

        // 5. Store masked payment details
        if (requestDTO.getPaymentMethod() == PaymentMethod.CREDIT_CARD ||
                requestDTO.getPaymentMethod() == PaymentMethod.DEBIT_CARD) {
            payment.setCardLast4Digits(maskCardNumber(requestDTO.getCardNumber()));
            payment.setCardBrand(detectCardBrand(requestDTO.getCardNumber()));
        } else if (requestDTO.getPaymentMethod() == PaymentMethod.UPI) {
            payment.setUpiId(requestDTO.getUpiId());
        } else if (requestDTO.getPaymentMethod() == PaymentMethod.NET_BANKING) {
            payment.setBankName(requestDTO.getBankName());
            payment.setAccountNumber(maskAccountNumber(requestDTO.getAccountNumber()));
        } else if (requestDTO.getPaymentMethod() == PaymentMethod.WALLET) {
            payment.setWalletProvider(requestDTO.getWalletProvider());
        }

        Payment savedPayment = paymentRepository.save(payment);

        // 6. Process payment based on method
        if (requestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            // COD doesn't need immediate processing
            savedPayment.setStatus(PaymentStatus.PENDING);
        } else {
            // Process payment through gateway
            processPaymentThroughGateway(savedPayment, requestDTO);
        }

        paymentRepository.save(savedPayment);

        log.info("Payment created: {}", savedPayment.getPaymentId());

        return mapToResponseDTO(savedPayment);
    }

    @Transactional
    public PaymentResponseDTO processPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStateException("Payment is not in PENDING status");
        }

        // Simulate payment processing
        payment.setStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);

        // Create transaction
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(TransactionType.CHARGE);
        transaction.setAmount(payment.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setRemarks("Payment processing initiated");
        payment.addTransaction(transaction);

        // Simulate gateway processing (90% success rate)
        boolean success = simulateGatewayProcessing();

        if (success) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            payment.setGatewayTransactionId("GW-" + UUID.randomUUID().toString());
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setGatewayTransactionId(payment.getGatewayTransactionId());
            transaction.setRemarks("Payment successful");
            log.info("Payment successful: {}", payment.getPaymentId());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailedAt(LocalDateTime.now());
            payment.setFailureReason("Gateway declined the transaction");
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setRemarks("Payment failed - Gateway declined");
            log.warn("Payment failed: {}", payment.getPaymentId());
        }

        Payment processedPayment = paymentRepository.save(payment);

        return mapToResponseDTO(processedPayment);
    }

    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToResponseDTO(payment);
    }

    public PaymentResponseDTO getPaymentByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with payment ID: " + paymentId));
        return mapToResponseDTO(payment);
    }

    public PaymentResponseDTO getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order ID: " + orderId));
        return mapToResponseDTO(payment);
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByDateRange(startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentResponseDTO confirmCODPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (payment.getPaymentMethod() != PaymentMethod.CASH_ON_DELIVERY) {
            throw new InvalidPaymentStateException("Only COD payments can be confirmed manually");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStateException("Payment is not in PENDING status");
        }

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(LocalDateTime.now());

        // Create transaction
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(TransactionType.CHARGE);
        transaction.setAmount(payment.getAmount());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRemarks("COD payment confirmed");
        payment.addTransaction(transaction);

        Payment confirmedPayment = paymentRepository.save(payment);

        log.info("COD Payment confirmed: {}", payment.getPaymentId());

        return mapToResponseDTO(confirmedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new InvalidPaymentStateException("Cannot delete completed payments");
        }

        paymentRepository.delete(payment);
        log.info("Payment deleted: {}", payment.getPaymentId());
    }

    private void processPaymentThroughGateway(Payment payment, PaymentRequestDTO requestDTO) {
        try {
            payment.setStatus(PaymentStatus.PROCESSING);

            // Simulate gateway API call
            boolean success = simulateGatewayProcessing();

            if (success) {
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setPaidAt(LocalDateTime.now());
                payment.setGatewayTransactionId("GW-" + UUID.randomUUID().toString());
                payment.setGatewayResponse("SUCCESS");

                // Create success transaction
                PaymentTransaction transaction = new PaymentTransaction();
                transaction.setTransactionId(generateTransactionId());
                transaction.setTransactionType(TransactionType.CHARGE);
                transaction.setAmount(payment.getAmount());
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setGatewayTransactionId(payment.getGatewayTransactionId());
                transaction.setRemarks("Payment processed successfully");
                payment.addTransaction(transaction);

            } else {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailedAt(LocalDateTime.now());
                payment.setFailureReason("Insufficient funds / Card declined");
                payment.setGatewayResponse("FAILED");

                // Create failed transaction
                PaymentTransaction transaction = new PaymentTransaction();
                transaction.setTransactionId(generateTransactionId());
                transaction.setTransactionType(TransactionType.CHARGE);
                transaction.setAmount(payment.getAmount());
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setRemarks("Payment failed - " + payment.getFailureReason());
                payment.addTransaction(transaction);
            }

        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailedAt(LocalDateTime.now());
            payment.setFailureReason("Gateway error: " + e.getMessage());
            log.error("Payment processing error: {}", e.getMessage());
        }
    }

    private boolean simulateGatewayProcessing() {
        // Simulate 90% success rate
        return random.nextInt(100) < 90;
    }

    private PaymentGateway determinePaymentGateway(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD, DEBIT_CARD -> PaymentGateway.STRIPE;
            case UPI, NET_BANKING, WALLET -> PaymentGateway.RAZORPAY;
            case PAYPAL -> PaymentGateway.PAYPAL;
            case CASH_ON_DELIVERY -> PaymentGateway.INTERNAL;
            default -> PaymentGateway.STRIPE;
        };
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return cardNumber.substring(cardNumber.length() - 4);
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return "****";
        return accountNumber.substring(accountNumber.length() - 4);
    }

    private String detectCardBrand(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) return "UNKNOWN";

        String firstDigit = cardNumber.substring(0, 1);
        return switch (firstDigit) {
            case "4" -> "VISA";
            case "5" -> "MASTERCARD";
            case "3" -> "AMEX";
            case "6" -> "DISCOVER";
            default -> "UNKNOWN";
        };
    }

    private String generatePaymentId() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = paymentRepository.count() + 1;
        String sequencePart = String.format("%05d", count);
        return "PAY-" + datePart + "-" + sequencePart;
    }

    private String generateTransactionId() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = String.format("%04d", random.nextInt(10000));
        return "TXN-" + datePart + "-" + randomPart;
    }

    private PaymentResponseDTO mapToResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(payment.getOrderId());
        dto.setOrderNumber(payment.getOrderNumber());
        dto.setUserId(payment.getUserId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentGateway(payment.getPaymentGateway());
        dto.setGatewayTransactionId(payment.getGatewayTransactionId());
        dto.setCardLast4Digits(payment.getCardLast4Digits());
        dto.setCardBrand(payment.getCardBrand());
        dto.setUpiId(payment.getUpiId());
        dto.setBankName(payment.getBankName());
        dto.setWalletProvider(payment.getWalletProvider());
        dto.setCustomerEmail(payment.getCustomerEmail());
        dto.setCustomerPhone(payment.getCustomerPhone());
        dto.setDescription(payment.getDescription());
        dto.setFailureReason(payment.getFailureReason());
        dto.setPaidAt(payment.getPaidAt());
        dto.setFailedAt(payment.getFailedAt());
        dto.setRefundedAt(payment.getRefundedAt());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        // Map transactions
        if (payment.getTransactions() != null && !payment.getTransactions().isEmpty()) {
            dto.setTransactions(payment.getTransactions().stream()
                    .map(this::mapTransactionToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private PaymentTransactionResponseDTO mapTransactionToDTO(PaymentTransaction transaction) {
        PaymentTransactionResponseDTO dto = new PaymentTransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setTransactionId(transaction.getTransactionId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setGatewayTransactionId(transaction.getGatewayTransactionId());
        dto.setRemarks(transaction.getRemarks());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
}