package com.ecommerce.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class RequestResponseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Add custom request ID
        String requestId = UUID.randomUUID().toString();
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Request-ID", requestId)
                .header("X-Request-Time", LocalDateTime.now().toString())
                .build();

        ServerHttpResponse response = exchange.getResponse();

        // Add custom response headers
        response.getHeaders().add("X-Request-ID", requestId);
        response.getHeaders().add("X-Processed-By", "API-Gateway");

        log.info("Request ID: {} | Path: {} | Method: {}",
                requestId, request.getPath(), request.getMethod());

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}