### **13\. Test the APIs**

**Create Category:**

```Bash

    POST http://localhost:8082/api/categories
    Content-Type: application/json
    
    {
      "name": "Electronics",
      "description": "Electronic devices and accessories",
      "slug": "electronics",
      "imageUrl": "https://example.com/electronics.jpg"
    }
```

**Create Sub-Category:**

```Bash

    POST http://localhost:8082/api/categories
    Content-Type: application/json
    
    {
      "name": "Smartphones",
      "description": "Mobile phones and accessories",
      "slug": "smartphones",
      "parentId": 1,
      "imageUrl": "https://example.com/smartphones.jpg"
    }
```
**Create Product:**

```Bash

    POST http://localhost:8082/api/products
    Content-Type: application/json
    
    {
      "name": "iPhone 15 Pro",
      "description": "Latest iPhone with A17 Pro chip",
      "sku": "IPH15PRO256",
      "price": 999.99,
      "discountPrice": 899.99,
      "stockQuantity": 50,
      "categoryId": 2,
      "brand": "Apple",
      "imageUrl": "https://example.com/iphone15pro.jpg",
      "weight": 0.221,
      "dimensions": "146.6 x 70.6 x 8.25 mm",
      "isFeatured": true,
      "sellerId": 1
    }
```

**Get All Products:**

```Bash

    GET http://localhost:8082/api/products
```

**Get Product by ID:**

```Bash

    GET http://localhost:8082/api/products/1
```

**Search Products:**

```Bash

    GET http://localhost:8082/api/products/search?keyword=iphone
```

**Get Products by Category:**

```Bash

    GET http://localhost:8082/api/products/category/2
```

**Get Products by Price Range:**

```Bash

    GET http://localhost:8082/api/products/price-range?minPrice=500&maxPrice=1000
```

**Get Featured Products:**

```Bash

    GET http://localhost:8082/api/products/featured
```

**Get Best Sellers:**

```Bash

    GET http://localhost:8082/api/products/best-sellers
```

**Get New Arrivals:**

```Bash

    GET http://localhost:8082/api/products/new-arrivals
```

**Update Product:**

```Bash

    PUT http://localhost:8082/api/products/1
    Content-Type: application/json
    
    {
      "name": "iPhone 15 Pro Max",
      "price": 1099.99,
      "stockQuantity": 75
    }
```

**Delete Product:**

```Bash

    DELETE http://localhost:8082/api/products/1
```

**Get All Categories:**

```Bash

    GET http://localhost:8082/api/categories
```

**Get Root Categories:**

```Bash

    GET http://localhost:8082/api/categories/root
```

**Get Sub-Categories:**

```Bash

    GET http://localhost:8082/api/categories/1/subcategories
```

**Update Category:**

```Bash

    PUT http://localhost:8082/api/categories/1
    Content-Type: application/json
    
    {
      "name": "Electronics & Gadgets",
      "description": "Updated description"
    }
```

**Delete Category:**

```Bash

    DELETE http://localhost:8082/api/categories/1
```

* * *




