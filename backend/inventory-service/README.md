### **13\. Test the APIs**

**Create Warehouse:**

```Bash

    POST http://localhost:8083/api/warehouses
    Content-Type: application/json
    
    {
      "code": "WH001",
      "name": "Main Warehouse",
      "address": "123 Warehouse Street",
      "city": "New York",
      "state": "NY",
      "country": "USA",
      "zipCode": "10001",
      "contactPerson": "John Manager",
      "contactPhone": "1234567890",
      "contactEmail": "warehouse@example.com"
    }
```

**Create Inventory (Make sure you have a product created in Product Service first):**

```Bash

    POST http://localhost:8083/api/inventory
    Content-Type: application/json
    
    {
      "productId": 1,
      "productName": "iPhone 15 Pro",
      "sku": "IPH15PRO256",
      "availableQuantity": 100,
      "reorderLevel": 20,
      "reorderQuantity": 50,
      "warehouseId": 1
    }
```

**Get All Inventory:**

```Bash

    GET http://localhost:8083/api/inventory
```

**Get Inventory by Product ID:**

```Bash

    GET http://localhost:8083/api/inventory/product/1
```

**Get Inventory by SKU:**

```Bash

    GET http://localhost:8083/api/inventory/sku/IPH15PRO256
```

**Get Low Stock Items:**

```Bash

    GET http://localhost:8083/api/inventory/low-stock
```

**Get Out of Stock Items:**

```Bash

    GET http://localhost:8083/api/inventory/out-of-stock
```

**Add Stock:**

```Bash

    POST http://localhost:8083/api/inventory/1/add-stock
    Content-Type: application/json
    
    {
      "quantity": 50,
      "reason": "PURCHASE",
      "notes": "New stock arrived",
      "performedBy": 1
    }
```

**Remove Stock:**

```Bash

    POST http://localhost:8083/api/inventory/1/remove-stock
    Content-Type: application/json
    
    {
      "quantity": 10,
      "reason": "DAMAGED",
      "notes": "Damaged items removed",
      "performedBy": 1
    }
```

**Reserve Stock (for Order):**

```Bash

    POST http://localhost:8083/api/inventory/reserve?productId=1&quantity=5&orderId=ORD001
```

**Release Reserved Stock (Order Cancelled):**

```Bash

    POST http://localhost:8083/api/inventory/release?productId=1&quantity=5&orderId=ORD001
```

**Confirm Reservation (Order Completed):**

```Bash

    POST http://localhost:8083/api/inventory/confirm?productId=1&quantity=5&orderId=ORD001
```

**Check Stock Availability:**

```Bash

    GET http://localhost:8083/api/inventory/check-availability?productId=1&quantity=10
```

**Get All Warehouses:**

```Bash

    GET http://localhost:8083/api/warehouses
```
**Get Warehouse by Code:**

```Bash

    GET http://localhost:8083/api/warehouses/code/WH001
```

**Update Warehouse:**

```Bash

    PUT http://localhost:8083/api/warehouses/1
    Content-Type: application/json
    
    {
      "name": "Main Distribution Center",
      "contactPerson": "Jane Manager"
    }
```

**Get Stock Movements by Inventory:**

```Bash

    GET http://localhost:8083/api/stock-movements/inventory/1
```

**Get Stock Movements by Reference (Order ID):**

```Bash

    GET http://localhost:8083/api/stock-movements/reference/ORD001
```

**Update Inventory:**

```Bash

    PUT http://localhost:8083/api/inventory/1
    Content-Type: application/json
    
    {
      "availableQuantity": 150,
      "reorderLevel": 30
    }
```

**Search Inventory:**

```Bash

    GET http://localhost:8083/api/inventory/search?keyword=iPhone
```

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

### **15\. Database Tables Created**

The following tables will be created automatically:

1.  **inventory** - Stores inventory records
2.  **warehouses** - Stores warehouse information
3.  **stock\_movements** - Stores all stock movement history

### **16\. Key Features**

✅ Inventory CRUD operations  
✅ Warehouse management  
✅ Stock reservation for orders  
✅ Stock release on order cancellation  
✅ Stock confirmation on order completion  
✅ Add/Remove stock with tracking  
✅ Low stock alerts  
✅ Out of stock detection  
✅ Stock movement history/audit trail  
✅ Reorder level management  
✅ Multi-warehouse support  
✅ Feign Client integration with Product Service  
✅ Automatic status updates (IN\_STOCK, LOW\_STOCK, OUT\_OF\_STOCK)

### **17\. Stock Flow Example**

**Scenario: Customer places an order**

1.  **Check Availability:**

```Bash

    GET /api/inventory/check-availability?productId=1&quantity=2
    # Response: true
```

2.  **Reserve Stock:**

```Bash

    POST /api/inventory/reserve?productId=1&quantity=2&orderId=ORD123
    # Stock moves from available to reserved
```

3.  **Confirm Order (Payment Success):**

```Bash

    POST /api/inventory/confirm?productId=1&quantity=2&orderId=ORD123
    # Reserved stock is deducted, stock movement recorded
```

**OR**

3.  **Cancel Order:**

```Bash

    POST /api/inventory/release?productId=1&quantity=2&orderId=ORD123
    # Reserved stock released back to available
```

### **18\. Feign Client Communication**

The Inventory Service communicates with Product Service via Feign Client:

```Java

    // When creating inventory
    ProductResponseDTO product = productClient.getProductById(requestDTO.getProductId());
    
    // When updating stock
    productClient.updateProductStock(productId, quantity);
```

**Note:** Make sure Product Service endpoints are accessible for Feign to work properly.

### **19\. Testing Feign Client Integration**

1.  **Create a product in Product Service first:**

```Bash

    POST http://localhost:8082/api/products
    Content-Type: application/json
    
    {
      "name": "Test Product",
      "sku": "TEST001",
      "price": 99.99,
      "stockQuantity": 0,
      "categoryId": 1,
      "brand": "TestBrand"
    }
```

2.  **Create inventory for that product:**

```Bash

    POST http://localhost:8083/api/inventory
    Content-Type: application/json
    
    {
      "productId": 1,
      "productName": "Test Product",
      "sku": "TEST001",
      "availableQuantity": 100,
      "warehouseId": 1
    }
```

The Inventory Service will automatically call Product Service to verify the product exists.

* * *

## **Inventory Service is Complete!** ✅

**What you've achieved:**

-   ✅ Inventory Service running on port 8083
-   ✅ Full inventory management
-   ✅ Warehouse management
-   ✅ Stock reservation system
-   ✅ Stock movement tracking
-   ✅ Feign Client integration with Product Service
-   ✅ Registered with Eureka Server



