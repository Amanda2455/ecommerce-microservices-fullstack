package com.ecommerce.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    // Optional: Define routes programmatically
    // Currently using YAML configuration, but you can also define routes here

    /*
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                .route("product-service", r -> r.path("/api/products/**")
                        .uri("lb://product-service"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .uri("lb://inventory-service"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .uri("lb://order-service"))
                .route("payment-service", r -> r.path("/api/payments/**")
                        .uri("lb://payment-service"))
                .build();
    }
    */
}