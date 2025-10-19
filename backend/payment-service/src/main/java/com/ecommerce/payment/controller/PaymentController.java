package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.PaymentRequestDTO;
import com.ecommerce.payment.dto.PaymentResponseDTO;
import com.ecommerce.payment.entity.PaymentStatus;
import com.ecommerce.payment.service.PaymentService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "APIs for managing payments, processing transactions, payment gateways, and financial operations")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(
            summary = "Create a new payment",
            description = "Initialize a new payment transaction for an order. Creates payment record with method details (credit card, PayPal, COD, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment created successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Payment already exists for this order"
            )
    })
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Parameter(description = "Payment details including order ID, amount, and payment method", required = true)
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO responseDTO = paymentService.createPayment(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/process")
    @Operation(
            summary = "Process a payment",
            description = "Process payment through the configured payment gateway. Handles authorization, capture, and status updates. Returns transaction ID and status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment processed successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payment cannot be processed (invalid status or already processed)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Payment gateway error or transaction declined"
            )
    })
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Parameter(description = "Payment ID", example = "1", required = true)
            @PathVariable Long id) {
        PaymentResponseDTO responseDTO = paymentService.processPayment(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get payment by ID",
            description = "Retrieve complete payment details including transaction status, amount, payment method, and gateway response by payment ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment found",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found with given ID"
            )
    })
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @Parameter(description = "Payment ID", example = "1", required = true)
            @PathVariable Long id) {
        PaymentResponseDTO responseDTO = paymentService.getPaymentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/payment-id/{paymentId}")
    @Operation(
            summary = "Get payment by payment ID",
            description = "Retrieve payment details using the unique payment identifier (e.g., PAY-20240115-00001). Used for payment tracking and reconciliation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment found",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found with given payment ID"
            )
    })
    public ResponseEntity<PaymentResponseDTO> getPaymentByPaymentId(
            @Parameter(description = "Unique payment identifier", example = "PAY-20240115-00001", required = true)
            @PathVariable String paymentId) {
        PaymentResponseDTO responseDTO = paymentService.getPaymentByPaymentId(paymentId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Get payment by order ID",
            description = "Retrieve payment information associated with a specific order. One order typically has one payment record."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment found for order",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found for given order ID"
            )
    })
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(
            @Parameter(description = "Order ID", example = "1", required = true)
            @PathVariable Long orderId) {
        PaymentResponseDTO responseDTO = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all payments",
            description = "Retrieve a complete list of all payment transactions in the system (Admin only in production)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All payments retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDTO.class))
            )
    )
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get payments by user ID",
            description = "Retrieve all payment transactions made by a specific user, sorted by date (newest first)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User payments retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDTO.class))
            )
    )
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByUserId(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long userId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get payments by status",
            description = "Filter payments by transaction status (PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, CANCELLED, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Payments filtered by status retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDTO.class))
            )
    )
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(
            @Parameter(description = "Payment status", example = "COMPLETED", required = true)
            @PathVariable PaymentStatus status) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get payments by date range",
            description = "Retrieve payments within a specified date range. Essential for financial reporting, reconciliation, and revenue analysis."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Payments within date range retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDTO.class))
            )
    )
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByDateRange(
            @Parameter(description = "Start date and time", example = "2024-01-01T00:00:00", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time", example = "2024-12-31T23:59:59", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/{id}/confirm-cod")
    @Operation(
            summary = "Confirm Cash on Delivery payment",
            description = "Confirm receipt of COD (Cash on Delivery) payment. Updates payment status to COMPLETED when cash is collected upon delivery."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "COD payment confirmed successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payment is not COD type or already confirmed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found with given ID"
            )
    })
    public ResponseEntity<PaymentResponseDTO> confirmCODPayment(
            @Parameter(description = "Payment ID", example = "1", required = true)
            @PathVariable Long id) {
        PaymentResponseDTO responseDTO = paymentService.confirmCODPayment(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a payment",
            description = "Permanently delete a payment record. Only FAILED or CANCELLED payments can be deleted. (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Payment deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Payment cannot be deleted (status is COMPLETED or PROCESSING)"
            )
    })
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "Payment ID", example = "1", required = true)
            @PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}


/**

package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.PaymentRequestDTO;
import com.ecommerce.payment.dto.PaymentResponseDTO;
import com.ecommerce.payment.entity.PaymentStatus;
import com.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO responseDTO = paymentService.createPayment(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable Long id) {
        PaymentResponseDTO responseDTO = paymentService.processPayment(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO responseDTO = paymentService.getPaymentById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/payment-id/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByPaymentId(@PathVariable String paymentId) {
        PaymentResponseDTO responseDTO = paymentService.getPaymentByPaymentId(paymentId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentResponseDTO responseDTO = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByUserId(@PathVariable Long userId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/{id}/confirm-cod")
    public ResponseEntity<PaymentResponseDTO> confirmCODPayment(@PathVariable Long id) {
        PaymentResponseDTO responseDTO = paymentService.confirmCODPayment(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
 */