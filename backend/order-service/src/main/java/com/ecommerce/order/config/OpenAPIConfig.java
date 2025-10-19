package com.ecommerce.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        Server server1 = new Server();
        server1.setUrl("http://localhost:8080");
        server1.setDescription("API Gateway");

        Server server2 = new Server();
        server2.setUrl("http://localhost:8084");
        server2.setDescription("Order Service Direct");

        Contact contact = new Contact();
        contact.setName("E-Commerce Support Team");
        contact.setEmail("support@ecommerce.com");
        contact.setUrl("https://www.ecommerce.com");

        License license = new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Order Service API")
                .version("1.0.0")
                .description("Order Management Service - Handles order creation, status tracking, and order workflow management")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server1, server2));
    }
}