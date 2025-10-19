package com.ecommerce.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("====== Request ======");
        log.info("Path: {}", exchange.getRequest().getPath());
        log.info("Method: {}", exchange.getRequest().getMethod());
        log.info("Headers: {}", exchange.getRequest().getHeaders());
        log.info("====================");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("====== Response ======");
            log.info("Status Code: {}", exchange.getResponse().getStatusCode());
            log.info("=====================");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute first
    }
}