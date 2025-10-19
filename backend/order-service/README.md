### **13\. Test the APIs**

* * *

### **Prerequisites: Create Test Data**

Before testing orders, ensure you have:

**1\. Create a User (User Service)**

```Bash

    POST http://localhost:8081/api/users
    Content-Type: application/json
    
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "password": "password123",
      "phoneNumber": "1234567890",
      "address": "123 Main St",
      "city": "New York",
      "state": "NY",
      "country": "USA",
      "zipCode": "10001",
      "role": "CUSTOMER"
    }
```
**2\. Create a Category (Product Service)**

```Bash

    POST http://localhost:8082/api/categories
    Content-Type: application/json
    
    {
      "name": "Electronics",
      "description": "Electronic devices",
      "slug": "electronics"
    }
```
**3\. Create Products (Product Service)**

```Bash

    POST http://localhost:8082/api/products
    Content-Type: application/json
    
    {
      "name": "iPhone 15 Pro",
      "description": "Latest iPhone",
      "sku": "IPH15PRO",
      "price": 999.99,
      "discountPrice": 899.99,
      "stockQuantity": 0,
      "categoryId": 1,
      "brand": "Apple",
      "imageUrl": "https://example.com/iphone.jpg"
    }
```
```Bash

    POST http://localhost:8082/api/products
    Content-Type: application/json
    
    {
      "name": "AirPods Pro",
      "description": "Wireless earbuds",
      "sku": "AIRPODSPRO",
      "price": 249.99,
      "stockQuantity": 0,
      "categoryId": 1,
      "brand": "Apple",
      "imageUrl": "https://example.com/airpods.jpg"
    }
```
**4\. Create Warehouse (Inventory Service)**

```Bash

    POST http://localhost:8083/api/warehouses
    Content-Type: application/json
    
    {
      "code": "WH001",
      "name": "Main Warehouse",
      "address": "123 Warehouse St",
      "city": "New York",
      "state": "NY",
      "country": "USA",
      "zipCode": "10001",
      "contactPerson": "Warehouse Manager",
      "contactPhone": "1234567890",
      "contactEmail": "warehouse@example.com"
    }
```
**5\. Create Inventory (Inventory Service)**

```Bash

    POST http://localhost:8083/api/inventory
    Content-Type: application/json
    
    {
      "productId": 1,
      "productName": "iPhone 15 Pro",
      "sku": "IPH15PRO",
      "availableQuantity": 100,
      "reorderLevel": 10,
      "reorderQuantity": 50,
      "warehouseId": 1
    }
```
```Bash

    POST http://localhost:8083/api/inventory
    Content-Type: application/json
    
    {
      "productId": 2,
      "productName": "AirPods Pro",
      "sku": "AIRPODSPRO",
      "availableQuantity": 200,
      "reorderLevel": 20,
      "reorderQuantity": 100,
      "warehouseId": 1
    }
```
* * *

## **ORDER SERVICE ENDPOINTS**

* * *

### **1\. Create Order (Single Product)**

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 1,
          "quantity": 2
        }
      ],
      "shippingFee": 10.00,
      "paymentMethod": "CREDIT_CARD",
      "shippingAddress": "123 Main St",
      "shippingCity": "New York",
      "shippingState": "NY",
      "shippingCountry": "USA",
      "shippingZipCode": "10001",
      "notes": "Please deliver between 9 AM - 5 PM"
    }
```
* * *

### **2\. Create Order (Multiple Products)**

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 1,
          "quantity": 1
        },
        {
          "productId": 2,
          "quantity": 2
        }
      ],
      "discountAmount": 50.00,
      "shippingFee": 15.00,
      "paymentMethod": "DEBIT_CARD",
      "shippingAddress": "456 Oak Avenue",
      "shippingCity": "Los Angeles",
      "shippingState": "CA",
      "shippingCountry": "USA",
      "shippingZipCode": "90001",
      "billingAddress": "456 Oak Avenue",
      "billingCity": "Los Angeles",
      "billingState": "CA",
      "billingCountry": "USA",
      "billingZipCode": "90001",
      "notes": "Gift wrap please"
    }
```
* * *

### **3\. Create Order (Cash on Delivery)**

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 2,
          "quantity": 3
        }
      ],
      "shippingFee": 5.00,
      "paymentMethod": "CASH_ON_DELIVERY",
      "shippingAddress": "789 Elm Street",
      "shippingCity": "Chicago",
      "shippingState": "IL",
      "shippingCountry": "USA",
      "shippingZipCode": "60601"
    }
```
* * *

### **4\. Get Order by ID**

```Bash

    GET http://localhost:8084/api/orders/1
```
* * *

### **5\. Get Order by Order Number**

```Bash

    GET http://localhost:8084/api/orders/order-number/ORD-20240115-00001
```
* * *

### **6\. Get All Orders**

```Bash

    GET http://localhost:8084/api/orders
```
* * *

### **7\. Get Orders by User ID**

```Bash

    GET http://localhost:8084/api/orders/user/1
```
* * *

### **8\. Get Orders by Status (Pending)**

```Bash

    GET http://localhost:8084/api/orders/status/PENDING
```
* * *

### **9\. Get Orders by Status (Confirmed)**

```Bash

    GET http://localhost:8084/api/orders/status/CONFIRMED
```
* * *

### **10\. Get Orders by Status (Shipped)**

```Bash

    GET http://localhost:8084/api/orders/status/SHIPPED
```
* * *

### **11\. Get Orders by Status (Delivered)**

```Bash

    GET http://localhost:8084/api/orders/status/DELIVERED
```
* * *

### **12\. Get Orders by Status (Cancelled)**

```Bash

    GET http://localhost:8084/api/orders/status/CANCELLED
```
* * *

### **13\. Get Orders by User and Status**

```Bash

    GET http://localhost:8084/api/orders/user/1/status/PENDING
```
* * *

### **14\. Get Orders by Date Range**

```Bash

    GET http://localhost:8084/api/orders/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```
* * *

### **15\. Get Orders by Email**

```Bash

    GET http://localhost:8084/api/orders/email/john.doe@example.com
```
* * *

### **16\. Update Order Status (Confirm Order - Payment Success)**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "CONFIRMED",
      "remarks": "Payment successful via Credit Card",
      "changedBy": 1
    }
```
* * *

### **17\. Update Order Status (Start Processing)**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "PROCESSING",
      "remarks": "Order is being prepared for shipment",
      "changedBy": 2
    }
```
* * *

### **18\. Update Order Status (Ship Order)**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "SHIPPED",
      "remarks": "Order shipped via FedEx, Tracking: FDX123456789",
      "changedBy": 2
    }
```
* * *

### **19\. Update Order Status (Deliver Order)**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "DELIVERED",
      "remarks": "Order delivered successfully",
      "changedBy": 2
    }
```
* * *

### **20\. Cancel Order (By Customer)**

```Bash

    POST http://localhost:8084/api/orders/2/cancel
    Content-Type: application/json
    
    {
      "reason": "Changed my mind, don't need it anymore",
      "cancelledBy": 1
    }
```
* * *

### **21\. Cancel Order (Out of Stock)**

```Bash

    POST http://localhost:8084/api/orders/3/cancel
    Content-Type: application/json
    
    {
      "reason": "Product out of stock",
      "cancelledBy": 2
    }
```
* * *

### **22\. Cancel Order (Payment Failed)**

```Bash

    POST http://localhost:8084/api/orders/4/cancel
    Content-Type: application/json
    
    {
      "reason": "Payment gateway error - transaction failed",
      "cancelledBy": 2
    }
```
* * *

### **23\. Get Order Status History**

```Bash

    GET http://localhost:8084/api/order-status-history/order/1
```
* * *

### **24\. Get All Order Status History**

```Bash

    GET http://localhost:8084/api/order-status-history
```
* * *

### **25\. Delete Order (Only Cancelled Orders)**

```Bash

    DELETE http://localhost:8084/api/orders/2
```
* * *

### **26\. Try to Delete Active Order (Will Fail)**

```Bash

    DELETE http://localhost:8084/api/orders/1
    # Expected Error: "Only cancelled orders can be deleted"
```
* * *

### **27\. Create Order with Insufficient Stock (Will Fail)**

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 1,
          "quantity": 1000
        }
      ],
      "shippingFee": 10.00,
      "paymentMethod": "CREDIT_CARD",
      "shippingAddress": "123 Main St",
      "shippingCity": "New York",
      "shippingState": "NY",
      "shippingCountry": "USA",
      "shippingZipCode": "10001"
    }
    # Expected Error: "Insufficient stock for product: iPhone 15 Pro"
```
* * *

### **28\. Create Order with Invalid User (Will Fail)** 

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 999,
      "customerName": "Invalid User",
      "customerEmail": "invalid@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 1,
          "quantity": 1
        }
      ],
      "shippingFee": 10.00,
      "paymentMethod": "CREDIT_CARD",
      "shippingAddress": "123 Main St",
      "shippingCity": "New York",
      "shippingState": "NY",
      "shippingCountry": "USA",
      "shippingZipCode": "10001"
    }
    # Expected Error: "User not found with id: 999"
```
* * *

### **29\. Create Order with Invalid Product (Will Fail)**

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 999,
          "quantity": 1
        }
      ],
      "shippingFee": 10.00,
      "paymentMethod": "CREDIT_CARD",
      "shippingAddress": "123 Main St",
      "shippingCity": "New York",
      "shippingState": "NY",
      "shippingCountry": "USA",
      "shippingZipCode": "10001"
    }
    # Expected Error: "Product not found with id: 999"
```
* * *

### **30\. Try Invalid Status Transition (Will Fail)**

```Bash

    # Try to ship a PENDING order (must be CONFIRMED first)
    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "SHIPPED",
      "remarks": "Trying invalid transition",
      "changedBy": 2
    }
    # Expected Error: "Invalid status transition from PENDING to SHIPPED"
```
* * *

## **Complete Order Workflow Example**

### **Scenario: Customer places and receives an order**

**Step 1: Customer creates order**

```Bash

    POST http://localhost:8084/api/orders
    Content-Type: application/json
    
    {
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "productId": 1,
          "quantity": 1
        }
      ],
      "shippingFee": 10.00,
      "paymentMethod": "CREDIT_CARD",
      "shippingAddress": "123 Main St",
      "shippingCity": "New York",
      "shippingState": "NY",
      "shippingCountry": "USA",
      "shippingZipCode": "10001"
    }
    # Response: Order created with status PENDING
    # Inventory: Stock RESERVED (availableQuantity reduced, reservedQuantity increased)
```
**Step 2: Check inventory status**

```Bash

    GET http://localhost:8083/api/inventory/product/1
    # Response: availableQuantity: 99, reservedQuantity: 1
```
**Step 3: Payment successful - Confirm order**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "CONFIRMED",
      "remarks": "Payment successful",
      "changedBy": 1
    }
    # Response: Order status changed to CONFIRMED
    # Inventory: Stock reservation CONFIRMED (reservedQuantity reduced, stock sold)
```
**Step 4: Check inventory after confirmation**

```Bash

    GET http://localhost:8083/api/inventory/product/1
    # Response: availableQuantity: 99, reservedQuantity: 0, totalQuantity: 99
```
**Step 5: Start processing order**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "PROCESSING",
      "remarks": "Order is being prepared",
      "changedBy": 2
    }
```
**Step 6: Ship the order**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "SHIPPED",
      "remarks": "Shipped via FedEx, Tracking: FDX123456789",
      "changedBy": 2
    }
```
**Step 7: Deliver the order**

```Bash

    PATCH http://localhost:8084/api/orders/1/status
    Content-Type: application/json
    
    {
      "status": "DELIVERED",
      "remarks": "Successfully delivered to customer",
      "changedBy": 2
    }
```
**Step 8: View order history**

```Bash

    GET http://localhost:8084/api/order-status-history/order/1
    # Shows complete status change history
```
* * *

## **Order Cancellation Workflow**

### **Scenario: Customer cancels order before confirmation**

**Step 1: Create order**

```Bash

    POST http://localhost:8084/api/orders
    # (Same as above)
    # Status: PENDING
    # Inventory: Stock RESERVED
```
**Step 2: Customer cancels order**

```Bash

    POST http://localhost:8084/api/orders/1/cancel
    Content-Type: application/json
    
    {
      "reason": "Changed my mind",
      "cancelledBy": 1
    }
    # Response: Order cancelled
    # Inventory: Reserved stock RELEASED back to available
```
**Step 3: Verify inventory restored**

```Bash

    GET http://localhost:8083/api/inventory/product/1
    # Response: availableQuantity: 100 (back to original)
```
* * *

## **Stock Movement Tracking**

**View stock movements for a product:**

```Bash

    GET http://localhost:8083/api/stock-movements/inventory/1
    # Shows all stock movements including:
    # - RESERVED (when order created)
    # - RELEASED (when order cancelled)
    # - OUT (when order confirmed)
```
* * *

## **Integration Flow Diagram**

```text

    Customer Places Order
            ↓
    ┌───────────────────────────────────────┐
    │      Order Service (8084)             │
    │  1. Validate User (User Service)      │
    │  2. Validate Products (Product Svc)   │
    │  3. Check Stock (Inventory Service)   │
    │  4. Calculate Totals                  │
    │  5. Create Order (PENDING)            │
    │  6. Reserve Stock (Inventory Svc)     │
    └───────────────────────────────────────┘
            ↓
    ┌───────────────────────────────────────┐
    │   Inventory Service (8083)            │
    │  - Reserve Stock for Order            │
    │  - Create Stock Movement (RESERVED)   │
    └───────────────────────────────────────┘
    
    Payment Successful
            ↓
    ┌───────────────────────────────────────┐
    │      Order Service (8084)             │
    │  Update Status → CONFIRMED            │
    │  Confirm Reservation (Inventory Svc)  │
    └───────────────────────────────────────┘
            ↓
    ┌───────────────────────────────────────┐
    │   Inventory Service (8083)            │
    │  - Confirm Reservation                │
    │  - Reduce Reserved Quantity           │
    │  - Create Stock Movement (OUT)        │
    └───────────────────────────────────────┘
    
    Order Processing → Shipped → Delivered
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
-   **ORDER-SERVICE** (Port 8084) ⭐ NEW

* * *

### **15\. Database Tables Created**

The following tables will be created automatically:

1.  **orders** - Main order information
2.  **order\_items** - Products in each order
3.  **order\_status\_history** - Order status change tracking

* * *

### **16\. Key Features**

✅ Order creation with multiple products  
✅ User validation via User Service  
✅ Product validation via Product Service  
✅ Stock availability check via Inventory Service  
✅ Automatic stock reservation  
✅ Stock confirmation on payment success  
✅ Stock release on order cancellation  
✅ Order status workflow (PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED)  
✅ Order cancellation  
✅ Tax calculation (10%)  
✅ Shipping fee support  
✅ Discount support  
✅ Order status history tracking  
✅ Separate shipping and billing addresses  
✅ Multiple payment methods  
✅ Auto-generated order numbers  
✅ Feign Client integration with 3 services

* * *

### **17\. Order Status Transitions**

```text

    PENDING ──────────→ CONFIRMED ──────→ PROCESSING ──────→ SHIPPED ──────→ DELIVERED
       │                    │                  │                 │
       │                    │                  │                 │
       └──→ CANCELLED       └──→ CANCELLED     └──→ CANCELLED    └──→ RETURNED
```
**Valid Transitions:**

-   PENDING → CONFIRMED, CANCELLED
-   CONFIRMED → PROCESSING, CANCELLED
-   PROCESSING → SHIPPED, CANCELLED
-   SHIPPED → DELIVERED, RETURNED
-   DELIVERED → RETURNED

**Terminal States:** CANCELLED, RETURNED, REFUNDED

* * *

### **18\. Sample Order Response**

```JSON

    {
      "id": 1,
      "orderNumber": "ORD-20240115-00001",
      "userId": 1,
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "1234567890",
      "orderItems": [
        {
          "id": 1,
          "productId": 1,
          "productName": "iPhone 15 Pro",
          "sku": "IPH15PRO",
          "quantity": 1,
          "unitPrice": 999.99,
          "discountPrice": 899.99,
          "totalPrice": 899.99,
          "productImageUrl": "https://example.com/iphone.jpg",
          "createdAt": "2024-01-15T10:30:00"
        }
      ],
      "subtotal": 899.99,
      "discountAmount": 0.00,
      "taxAmount": 89.99,
      "shippingFee": 10.00,
      "totalAmount": 999.98,
      "status": "PENDING",
      "paymentStatus": "PENDING",
      "paymentMethod": "CREDIT_CARD",
      "paymentTransactionId": null,
      "shippingAddress": "123 Main St",
      "shippingCity": "New York",
      "shippingState": "NY",
      "shippingCountry": "USA",
      "shippingZipCode": "10001",
      "billingAddress": "123 Main St",
      "billingCity": "New York",
      "billingState": "NY",
      "billingCountry": "USA",
      "billingZipCode": "10001",
      "notes": "Please deliver between 9 AM - 5 PM",
      "confirmedAt": null,
      "shippedAt": null,
      "deliveredAt": null,
      "cancelledAt": null,
      "cancellationReason": null,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
```
* * *

## **Order Service is Complete!** ✅

**What you've achieved:**

-   ✅ Order Service running on port 8084
-   ✅ Complete order management system
-   ✅ Integration with User, Product, and Inventory services via Feign Clients
-   ✅ Stock reservation and confirmation workflow
-   ✅ Order status tracking with history
-   ✅ Tax and shipping calculations
-   ✅ Order cancellation with stock release
-   ✅ Registered with Eureka Server

