package com.ecommerce.payment.entity;

public enum PaymentGateway {
    STRIPE,
    PAYPAL,
    RAZORPAY,
    SQUARE,
    BRAINTREE,
    INTERNAL // For COD and internal processing
}