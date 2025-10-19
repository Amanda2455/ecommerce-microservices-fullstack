### **13\. Test the APIs**

**Create User:**

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

**Get User by ID:**

```Bash

    GET http://localhost:8081/api/users/1
```

**Get All Users:**

```Bash

    GET http://localhost:8081/api/users
```

**Update User:**

```Bash

    PUT http://localhost:8081/api/users/1
    Content-Type: application/json
    
    {
      "firstName": "John Updated",
      "city": "Los Angeles"
    }
```

**Delete User:**

```Bash

    DELETE http://localhost:8081/api/users/1
```




