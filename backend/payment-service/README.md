### **13\. Test the APIs**

* * *

### **Prerequisites: Create an Order First**

Before testing payments, create an order using Order Service (refer to Order Service documentation).

* * *

## **PAYMENT SERVICE ENDPOINTS**

* * *

### **1\. Create Payment (Credit Card)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 1,
      "orderNumber": "ORD-20240115-00001",
      "userId": 1,
      "amount": 1989.97,
      "currency": "USD",
      "paymentMethod": "CREDIT_CARD",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "cardNumber": "4111111111111111",
      "cardHolderName": "John Doe",
      "cardExpiryMonth": "12",
      "cardExpiryYear": "2025",
      "cardCvv": "123",
      "description": "Payment for Order ORD-20240115-00001"
    }
```
* * *

### **2\. Create Payment (UPI)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 2,
      "orderNumber": "ORD-20240115-00002",
      "userId": 1,
      "amount": 2500.00,
      "currency": "USD",
      "paymentMethod": "UPI",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "upiId": "john@okicici",
      "description": "Payment for Order ORD-20240115-00002"
    }
```
* * *

### **3\. Create Payment (Debit Card)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 3,
      "orderNumber": "ORD-20240115-00003",
      "userId": 1,
      "amount": 1500.00,
      "currency": "USD",
      "paymentMethod": "DEBIT_CARD",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "cardNumber": "5500000000000004",
      "cardHolderName": "John Doe",
      "cardExpiryMonth": "06",
      "cardExpiryYear": "2026",
      "cardCvv": "456",
      "description": "Payment for Order ORD-20240115-00003"
    }
```
* * *

### **4\. Create Payment (Net Banking)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 4,
      "orderNumber": "ORD-20240115-00004",
      "userId": 1,
      "amount": 3000.00,
      "currency": "USD",
      "paymentMethod": "NET_BANKING",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "bankName": "HDFC Bank",
      "accountNumber": "123456789012",
      "ifscCode": "HDFC0001234",
      "description": "Payment for Order ORD-20240115-00004"
    }
```
* * *

### **5\. Create Payment (Wallet)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 5,
      "orderNumber": "ORD-20240115-00005",
      "userId": 1,
      "amount": 1200.00,
      "currency": "USD",
      "paymentMethod": "WALLET",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "walletProvider": "PayTM",
      "description": "Payment for Order ORD-20240115-00005"
    }
```
* * *

### **6\. Create Payment (Cash on Delivery)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 6,
      "orderNumber": "ORD-20240115-00006",
      "userId": 1,
      "amount": 800.00,
      "currency": "USD",
      "paymentMethod": "CASH_ON_DELIVERY",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "description": "Payment for Order ORD-20240115-00006"
    }
```
* * *

### **7\. Create Payment (PayPal)**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 7,
      "orderNumber": "ORD-20240115-00007",
      "userId": 1,
      "amount": 2200.00,
      "currency": "USD",
      "paymentMethod": "PAYPAL",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "description": "Payment for Order ORD-20240115-00007"
    }
```
* * *

### **8\. Process Payment (Manual Processing)**

```Bash

    POST http://localhost:8085/api/payments/1/process
```
* * *

### **9\. Get Payment by ID**

```Bash

    GET http://localhost:8085/api/payments/1
```
* * *

### **10\. Get Payment by Payment ID**

```Bash

    GET http://localhost:8085/api/payments/payment-id/PAY-20240115-00001
```
* * *

### **11\. Get Payment by Order ID**

```Bash

    GET http://localhost:8085/api/payments/order/1
```
* * *

### **12\. Get All Payments**

```Bash

    GET http://localhost:8085/api/payments
```
* * *

### **13\. Get Payments by User ID**

```Bash

    GET http://localhost:8085/api/payments/user/1
```
* * *

### **14\. Get Payments by Status (Completed)**

```Bash

    GET http://localhost:8085/api/payments/status/COMPLETED
```
* * *

### **15\. Get Payments by Status (Pending)**

```Bash

    GET http://localhost:8085/api/payments/status/PENDING
```
* * *

### **16\. Get Payments by Status (Failed)**

```Bash

    GET http://localhost:8085/api/payments/status/FAILED
```
* * *

### **17\. Get Payments by Date Range**

```Bash

    GET http://localhost:8085/api/payments/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```
* * *

### **18\. Confirm COD Payment (Delivery Completed)**

```Bash

    POST http://localhost:8085/api/payments/6/confirm-cod
```
* * *

### **19\. Delete Payment (Only Failed/Cancelled)**

```Bash

    DELETE http://localhost:8085/api/payments/5
```
* * *

## **REFUND ENDPOINTS**

* * *

### **20\. Create Refund (Full Refund)**

```Bash

    POST http://localhost:8085/api/refunds
    Content-Type: application/json
    
    {
      "paymentId": 1,
      "amount": 1989.97,
      "reason": "ORDER_CANCELLED",
      "remarks": "Customer cancelled the order",
      "initiatedBy": 1
    }
```
* * *

### **21\. Create Refund (Partial Refund)**

```Bash

    POST http://localhost:8085/api/refunds
    Content-Type: application/json
    
    {
      "paymentId": 2,
      "amount": 500.00,
      "reason": "PRODUCT_DEFECTIVE",
      "remarks": "One item was defective",
      "initiatedBy": 2
    }
```
* * *

### **22\. Create Refund (Product Return)**

```Bash

    POST http://localhost:8085/api/refunds
    Content-Type: application/json
    
    {
      "paymentId": 3,
      "amount": 1500.00,
      "reason": "PRODUCT_RETURN",
      "remarks": "Customer returned the product",
      "initiatedBy": 1
    }

```

* * * 

### **23\. Process Refund**

```Bash

    POST http://localhost:8085/api/refunds/1/process
```
* * *

### **24\. Get Refund by ID**

```Bash

    GET http://localhost:8085/api/refunds/1
```
* * *

### **25\. Get Refund by Refund ID**

```Bash

    GET http://localhost:8085/api/refunds/refund-id/REF-20240115-00001
```
* * *

### **26\. Get Refunds by Payment ID**

```Bash

    GET http://localhost:8085/api/refunds/payment/1
```
* * *

### **27\. Get Refunds by Order ID**

```Bash

    GET http://localhost:8085/api/refunds/order/1
```
* * *

### **28\. Get Refunds by Status (Completed)**

```Bash

    GET http://localhost:8085/api/refunds/status/COMPLETED
```
* * *

### **29\. Get Refunds by Status (Pending)**

```Bash

    GET http://localhost:8085/api/refunds/status/PENDING
```
* * *

### **30\. Get All Refunds**

```Bash

    GET http://localhost:8085/api/refunds
```
* * *

### **31\. Cancel Refund**

```Bash

    POST http://localhost:8085/api/refunds/2/cancel
```
* * *

## **Complete Payment Workflow Example**

### **Scenario 1: Successful Payment Flow**

**Step 1: Create Order (Order Service)**

```Bash

    POST http://localhost:8084/api/orders
    # Creates order with status PENDING
```
**Step 2: Create Payment**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 1,
      "orderNumber": "ORD-20240115-00001",
      "userId": 1,
      "amount": 1989.97,
      "paymentMethod": "CREDIT_CARD",
      "customerEmail": "john.doe@example.com",
      "cardNumber": "4111111111111111",
      "cardHolderName": "John Doe",
      "cardExpiryMonth": "12",
      "cardExpiryYear": "2025",
      "cardCvv": "123"
    }
    # Payment created and automatically processed (90% success rate)
    # Status: COMPLETED or FAILED
```
**Step 3: Check Payment Status**

```Bash

    GET http://localhost:8085/api/payments/order/1
    # Verify payment status
```
**Step 4: If Payment Successful - Update Order (Order Service)**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "CONFIRMED",
      "remarks": "Payment successful"
    }
```
* * *

### **Scenario 2: COD Payment Flow**

**Step 1: Create Order**

```Bash

    POST http://localhost:8084/api/orders
    # Creates order
```
**Step 2: Create COD Payment**

```Bash

    POST http://localhost:8085/api/payments
    Content-Type: application/json
    
    {
      "orderId": 6,
      "orderNumber": "ORD-20240115-00006",
      "userId": 1,
      "amount": 800.00,
      "paymentMethod": "CASH_ON_DELIVERY",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890"
    }
    # Payment created with status PENDING
```
**Step 3: Ship Order (Order Service)**

```Bash

    PATCH http://localhost:8084/api/orders/6/status
    Content-Type: application/json
    
    {
      "status": "SHIPPED",
      "remarks": "Order shipped"
    }
```
**Step 4: Deliver Order (Order Service)**

```Bash

    PATCH http://localhost:8084/api/orders/6/status
    Content-Type: application/json
    
    {
      "status": "DELIVERED",
      "remarks": "Order delivered, COD collected"
    }
```
**Step 5: Confirm COD Payment (Payment Service)**

```Bash

    POST http://localhost:8085/api/payments/6/confirm-cod
    # Payment status changed to COMPLETED
```
* * *

### **Scenario 3: Refund Flow**

**Step 1: Customer Cancels Order**

```Bash

    POST http://localhost:8084/api/orders/1/cancel
    Content-Type: application/json
    
    {
      "reason": "Changed my mind",
      "cancelledBy": 1
    }
```
**Step 2: Create Refund**

```Bash

    POST http://localhost:8085/api/refunds
    Content-Type: application/json
    
    {
      "paymentId": 1,
      "amount": 1989.97,
      "reason": "ORDER_CANCELLED",
      "remarks": "Order cancelled by customer",
      "initiatedBy": 2
    }
    # Refund created with status PENDING
```
**Step 3: Process Refund**

```Bash

    POST http://localhost:8085/api/refunds/1/process
    # Refund processed (95% success rate)
    # Status: COMPLETED or FAILED
    # Payment status updated to REFUNDED
```
**Step 4: Verify Refund**

```Bash

    GET http://localhost:8085/api/refunds/payment/1
    # Check refund status
```
* * *

### **Scenario 4: Partial Refund Flow**

**Step 1: Create Refund for Part of Payment**

```Bash

    POST http://localhost:8085/api/refunds
    Content-Type: application/json
    
    {
      "paymentId": 2,
      "amount": 500.00,
      "reason": "PRODUCT_DEFECTIVE",
      "remarks": "One item defective, partial refund",
      "initiatedBy": 2
    }
```
**Step 2: Process Refund**

```Bash

    POST http://localhost:8085/api/refunds/2/process
    # Payment status updated to PARTIALLY_REFUNDED
```
**Step 3: Create Another Partial Refund (if needed)**

```Bash

    POST http://localhost:8085/api/refunds
    Content-Type: application/json
    
    {
      "paymentId": 2,
      "amount": 300.00,
      "reason": "CUSTOMER_REQUEST",
      "remarks": "Additional refund requested",
      "initiatedBy": 2
    }
```
* * *

### **14\. Verify Eureka Registration**

Open browser and go to:

```text

    http://localhost:8761
```
You should see all services registered:

-   **EUREKA-SERVER** (Port 8761)
-   **USER-SERVICE** (Port 8081)
-   **PRODUCT-SERVICE** (Port 8082)
-   **INVENTORY-SERVICE** (Port 8083)
-   **ORDER-SERVICE** (Port 8084)
-   **PAYMENT-SERVICE** (Port 8085) ⭐ NEW

* * *

### **15\. Database Tables Created**

The following tables will be created automatically:

1.  **payments** - Payment records
2.  **payment\_transactions** - Transaction history
3.  **refunds** - Refund records

* * *

### **16\. Key Features**

✅ Multiple payment methods (Credit Card, Debit Card, UPI, Net Banking, Wallet, COD, PayPal)  
✅ Payment gateway simulation (Stripe, Razorpay, PayPal)  
✅ Automatic payment processing (90% success rate simulation)  
✅ Payment status tracking (PENDING → PROCESSING → COMPLETED/FAILED)  
✅ Transaction history tracking  
✅ COD payment confirmation  
✅ Full and partial refunds  
✅ Refund processing with gateway simulation  
✅ Card number masking (security)  
✅ Card brand detection (VISA, MASTERCARD, AMEX)  
✅ UPI, Bank, Wallet details storage  
✅ Auto-generated payment/transaction/refund IDs  
✅ Integration with Order Service via Feign Client

* * *

### **17\. Payment Status Flow**

```text

    PENDING ──────→ PROCESSING ──────→ COMPLETED
                         │
                         └──────────→ FAILED
    
    COMPLETED ──────→ PARTIALLY_REFUNDED ──────→ REFUNDED
```
* * *

### **18\. Sample Payment Response**

```JSON

    {
      "id": 1,
      "paymentId": "PAY-20240115-00001",
      "orderId": 1,
      "orderNumber": "ORD-20240115-00001",
      "userId": 1,
      "amount": 1989.97,
      "currency": "USD",
      "paymentMethod": "CREDIT_CARD",
      "status": "COMPLETED",
      "paymentGateway": "STRIPE",
      "gatewayTransactionId": "GW-a7b3c4d5-e6f7-8901-2345-6789abcdef01",
      "cardLast4Digits": "1111",
      "cardBrand": "VISA",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "description": "Payment for Order ORD-20240115-00001",
      "paidAt": "2024-01-15T10:35:00",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:35:00",
      "transactions": [
        {
          "id": 1,
          "transactionId": "TXN-20240115103000-1234",
          "transactionType": "CHARGE",
          "amount": 1989.97,
          "status": "SUCCESS",
          "gatewayTransactionId": "GW-a7b3c4d5-e6f7-8901-2345-6789abcdef01",
          "remarks": "Payment processed successfully",
          "createdAt": "2024-01-15T10:30:00"
        }
      ]
    }
```
* * *

### **19\. Sample Refund Response**

```JSON

    {
      "id": 1,
      "refundId": "REF-20240115-00001",
      "paymentId": 1,
      "orderId": 1,
      "amount": 1989.97,
      "refundedAmount": 1989.97,
      "status": "COMPLETED",
      "reason": "ORDER_CANCELLED",
      "gatewayRefundId": "REF-GW-b8c4d5e6-f7a8-9012-3456-789abcdef012",
      "remarks": "Customer cancelled the order",
      "initiatedBy": 1,
      "processedAt": "2024-01-15T11:00:00",
      "createdAt": "2024-01-15T10:45:00",
      "updatedAt": "2024-01-15T11:00:00"
    }
```
* * *

### **20\. Payment Gateway Simulation**

The service simulates different payment gateways:

**Stripe (Credit/Debit Cards):**

-   Success Rate: 90%
-   Response Time: Instant
-   Transaction ID Format: `GW-UUID`

**Razorpay (UPI/Net Banking/Wallet):**

-   Success Rate: 90%
-   Response Time: Instant
-   Transaction ID Format: `GW-UUID`

**PayPal:**

-   Success Rate: 90%
-   Response Time: Instant
-   Transaction ID Format: `GW-UUID`

**Internal (COD):**

-   Manual confirmation required
-   No automatic processing



* * *