package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.RefundRequestDTO;
import com.ecommerce.payment.dto.RefundResponseDTO;
import com.ecommerce.payment.entity.RefundStatus;
import com.ecommerce.payment.service.RefundService;
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
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
@Tag(name = "Refund Management", description = "APIs for managing refunds, processing refund requests, partial refunds, and refund tracking")
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    @Operation(
            summary = "Create a new refund",
            description = "Initiate a refund request for a payment. Supports full and partial refunds with reason tracking. Links to original payment and order."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Refund created successfully",
                    content = @Content(schema = @Schema(implementation = RefundResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body, validation errors, or refund amount exceeds payment amount"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment or order not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Payment already fully refunded or refund not allowed for payment status"
            )
    })
    public ResponseEntity<RefundResponseDTO> createRefund(
            @Parameter(description = "Refund details including payment ID, amount, and reason", required = true)
            @Valid @RequestBody RefundRequestDTO requestDTO) {
        RefundResponseDTO responseDTO = refundService.createRefund(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/process")
    @Operation(
            summary = "Process a refund",
            description = "Process the refund through payment gateway. Initiates fund return to customer's original payment method. Updates refund status based on gateway response."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refund processed successfully",
                    content = @Content(schema = @Schema(implementation = RefundResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Refund cannot be processed (invalid status or already processed)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Refund not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Payment gateway error or refund processing failed"
            )
    })
    public ResponseEntity<RefundResponseDTO> processRefund(
            @Parameter(description = "Refund ID", example = "1", required = true)
            @PathVariable Long id) {
        RefundResponseDTO responseDTO = refundService.processRefund(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get refund by ID",
            description = "Retrieve complete refund details including amount, status, reason, and gateway transaction information by refund ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refund found",
                    content = @Content(schema = @Schema(implementation = RefundResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Refund not found with given ID"
            )
    })
    public ResponseEntity<RefundResponseDTO> getRefundById(
            @Parameter(description = "Refund ID", example = "1", required = true)
            @PathVariable Long id) {
        RefundResponseDTO responseDTO = refundService.getRefundById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/refund-id/{refundId}")
    @Operation(
            summary = "Get refund by refund ID",
            description = "Retrieve refund details using the unique refund identifier (e.g., REF-20240115-00001). Used for refund tracking and customer service."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refund found",
                    content = @Content(schema = @Schema(implementation = RefundResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Refund not found with given refund ID"
            )
    })
    public ResponseEntity<RefundResponseDTO> getRefundByRefundId(
            @Parameter(description = "Unique refund identifier", example = "REF-20240115-00001", required = true)
            @PathVariable String refundId) {
        RefundResponseDTO responseDTO = refundService.getRefundByRefundId(refundId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/payment/{paymentId}")
    @Operation(
            summary = "Get refunds by payment ID",
            description = "Retrieve all refund transactions associated with a specific payment. A payment can have multiple partial refunds."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Refunds for payment retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RefundResponseDTO.class))
            )
    )
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByPaymentId(
            @Parameter(description = "Payment ID", example = "1", required = true)
            @PathVariable Long paymentId) {
        List<RefundResponseDTO> refunds = refundService.getRefundsByPaymentId(paymentId);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Get refunds by order ID",
            description = "Retrieve all refund transactions for a specific order. Useful for order-level refund history and customer service."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Refunds for order retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RefundResponseDTO.class))
            )
    )
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByOrderId(
            @Parameter(description = "Order ID", example = "1", required = true)
            @PathVariable Long orderId) {
        List<RefundResponseDTO> refunds = refundService.getRefundsByOrderId(orderId);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get refunds by status",
            description = "Filter refunds by processing status (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REJECTED, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Refunds filtered by status retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RefundResponseDTO.class))
            )
    )
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByStatus(
            @Parameter(description = "Refund status", example = "COMPLETED", required = true)
            @PathVariable RefundStatus status) {
        List<RefundResponseDTO> refunds = refundService.getRefundsByStatus(status);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping
    @Operation(
            summary = "Get all refunds",
            description = "Retrieve a complete list of all refund transactions in the system (Admin only in production)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All refunds retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RefundResponseDTO.class))
            )
    )
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {
        List<RefundResponseDTO> refunds = refundService.getAllRefunds();
        return ResponseEntity.ok(refunds);
    }

    @PostMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel a refund request",
            description = "Cancel a pending refund request before it is processed. Only PENDING refunds can be cancelled. Already processed refunds cannot be cancelled."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Refund cancelled successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Refund cannot be cancelled (already processed or invalid status)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Refund not found with given ID"
            )
    })
    public ResponseEntity<Void> cancelRefund(
            @Parameter(description = "Refund ID", example = "1", required = true)
            @PathVariable Long id) {
        refundService.cancelRefund(id);
        return ResponseEntity.noContent().build();
    }
}


/**

package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.RefundRequestDTO;
import com.ecommerce.payment.dto.RefundResponseDTO;
import com.ecommerce.payment.entity.RefundStatus;
import com.ecommerce.payment.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundResponseDTO> createRefund(@Valid @RequestBody RefundRequestDTO requestDTO) {
        RefundResponseDTO responseDTO = refundService.createRefund(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<RefundResponseDTO> processRefund(@PathVariable Long id) {
        RefundResponseDTO responseDTO = refundService.processRefund(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundResponseDTO> getRefundById(@PathVariable Long id) {
        RefundResponseDTO responseDTO = refundService.getRefundById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/refund-id/{refundId}")
    public ResponseEntity<RefundResponseDTO> getRefundByRefundId(@PathVariable String refundId) {
        RefundResponseDTO responseDTO = refundService.getRefundByRefundId(refundId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByPaymentId(@PathVariable Long paymentId) {
        List<RefundResponseDTO> refunds = refundService.getRefundsByPaymentId(paymentId);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByOrderId(@PathVariable Long orderId) {
        List<RefundResponseDTO> refunds = refundService.getRefundsByOrderId(orderId);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByStatus(@PathVariable RefundStatus status) {
        List<RefundResponseDTO> refunds = refundService.getRefundsByStatus(status);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {
        List<RefundResponseDTO> refunds = refundService.getAllRefunds();
        return ResponseEntity.ok(refunds);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRefund(@PathVariable Long id) {
        refundService.cancelRefund(id);
        return ResponseEntity.noContent().build();
    }
}

 */