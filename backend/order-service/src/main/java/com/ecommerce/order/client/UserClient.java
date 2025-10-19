package com.ecommerce.order.client;

import com.ecommerce.order.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable Long id);

    @GetMapping("/api/users/email/{email}")
    UserResponseDTO getUserByEmail(@PathVariable String email);
}