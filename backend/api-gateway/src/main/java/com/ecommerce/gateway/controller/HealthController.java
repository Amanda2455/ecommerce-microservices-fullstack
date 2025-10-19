package com.ecommerce.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
@Tag(name = "Gateway Health & Discovery", description = "APIs for monitoring API Gateway health, service discovery status, and route information")
public class HealthController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/health")
    @Operation(
            summary = "API Gateway health check",
            description = "Check the health status of the API Gateway. Returns UP status when gateway is operational. Used by monitoring tools and load balancers."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Gateway is healthy and operational",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"status\": \"UP\",\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"service\": \"API Gateway\",\n" +
                                    "  \"port\": 8080\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "API Gateway");
        response.put("port", 8080);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/services")
    @Operation(
            summary = "Get registered services",
            description = "Retrieve all microservices registered with the service discovery (Eureka/Consul). Shows service names, instance counts, and discovery status. (Admin/DevOps only)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of registered services retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"totalServices\": 5,\n" +
                                    "  \"services\": [\"user-service\", \"product-service\", \"inventory-service\", \"order-service\", \"payment-service\"],\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"serviceDetails\": {\n" +
                                    "    \"user-service\": \"2 instance(s)\",\n" +
                                    "    \"product-service\": \"3 instance(s)\",\n" +
                                    "    \"inventory-service\": \"2 instance(s)\",\n" +
                                    "    \"order-service\": \"2 instance(s)\",\n" +
                                    "    \"payment-service\": \"1 instance(s)\"\n" +
                                    "  }\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> getRegisteredServices() {
        List<String> services = discoveryClient.getServices();

        Map<String, Object> response = new HashMap<>();
        response.put("totalServices", services.size());
        response.put("services", services);
        response.put("timestamp", LocalDateTime.now());

        Map<String, Object> serviceDetails = new HashMap<>();
        for (String service : services) {
            serviceDetails.put(service, discoveryClient.getInstances(service).size() + " instance(s)");
        }
        response.put("serviceDetails", serviceDetails);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    @Operation(
            summary = "Get API Gateway information",
            description = "Retrieve comprehensive gateway information including version, configured routes, and microservice mappings. Useful for API documentation and troubleshooting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Gateway information retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"name\": \"E-Commerce API Gateway\",\n" +
                                    "  \"version\": \"1.0.0\",\n" +
                                    "  \"description\": \"Single entry point for all E-Commerce microservices\",\n" +
                                    "  \"port\": 8080,\n" +
                                    "  \"timestamp\": \"2024-01-15T10:30:00\",\n" +
                                    "  \"routes\": {\n" +
                                    "    \"/api/users/**\": \"User Service (8081)\",\n" +
                                    "    \"/api/products/**\": \"Product Service (8082)\",\n" +
                                    "    \"/api/categories/**\": \"Product Service (8082)\",\n" +
                                    "    \"/api/inventory/**\": \"Inventory Service (8083)\",\n" +
                                    "    \"/api/warehouses/**\": \"Inventory Service (8083)\",\n" +
                                    "    \"/api/stock-movements/**\": \"Inventory Service (8083)\",\n" +
                                    "    \"/api/orders/**\": \"Order Service (8084)\",\n" +
                                    "    \"/api/order-status-history/**\": \"Order Service (8084)\",\n" +
                                    "    \"/api/payments/**\": \"Payment Service (8085)\",\n" +
                                    "    \"/api/refunds/**\": \"Payment Service (8085)\"\n" +
                                    "  }\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> getGatewayInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "E-Commerce API Gateway");
        response.put("version", "1.0.0");
        response.put("description", "Single entry point for all E-Commerce microservices");
        response.put("port", 8080);
        response.put("timestamp", LocalDateTime.now());

        Map<String, String> routes = new HashMap<>();
        routes.put("/api/users/**", "User Service (8081)");
        routes.put("/api/products/**", "Product Service (8082)");
        routes.put("/api/categories/**", "Product Service (8082)");
        routes.put("/api/inventory/**", "Inventory Service (8083)");
        routes.put("/api/warehouses/**", "Inventory Service (8083)");
        routes.put("/api/stock-movements/**", "Inventory Service (8083)");
        routes.put("/api/orders/**", "Order Service (8084)");
        routes.put("/api/order-status-history/**", "Order Service (8084)");
        routes.put("/api/payments/**", "Payment Service (8085)");
        routes.put("/api/refunds/**", "Payment Service (8085)");

        response.put("routes", routes);

        return ResponseEntity.ok(response);
    }
}




/**

package com.ecommerce.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
public class HealthController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "API Gateway");
        response.put("port", 8080);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getRegisteredServices() {
        List<String> services = discoveryClient.getServices();

        Map<String, Object> response = new HashMap<>();
        response.put("totalServices", services.size());
        response.put("services", services);
        response.put("timestamp", LocalDateTime.now());

        Map<String, Object> serviceDetails = new HashMap<>();
        for (String service : services) {
            serviceDetails.put(service, discoveryClient.getInstances(service).size() + " instance(s)");
        }
        response.put("serviceDetails", serviceDetails);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getGatewayInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "E-Commerce API Gateway");
        response.put("version", "1.0.0");
        response.put("description", "Single entry point for all E-Commerce microservices");
        response.put("port", 8080);
        response.put("timestamp", LocalDateTime.now());

        Map<String, String> routes = new HashMap<>();
        routes.put("/api/users/**", "User Service (8081)");
        routes.put("/api/products/**", "Product Service (8082)");
        routes.put("/api/categories/**", "Product Service (8082)");
        routes.put("/api/inventory/**", "Inventory Service (8083)");
        routes.put("/api/warehouses/**", "Inventory Service (8083)");
        routes.put("/api/stock-movements/**", "Inventory Service (8083)");
        routes.put("/api/orders/**", "Order Service (8084)");
        routes.put("/api/order-status-history/**", "Order Service (8084)");
        routes.put("/api/payments/**", "Payment Service (8085)");
        routes.put("/api/refunds/**", "Payment Service (8085)");

        response.put("routes", routes);

        return ResponseEntity.ok(response);
    }
}

*/