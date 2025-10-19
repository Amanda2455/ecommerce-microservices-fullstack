package com.ecommerce.payment.exception;

public class InsufficientRefundAmountException extends RuntimeException {
    public InsufficientRefundAmountException(String message) {
        super(message);
    }
}