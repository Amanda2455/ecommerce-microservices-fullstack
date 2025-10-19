package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequestDTO;
import com.ecommerce.user.dto.UserResponseDTO;
import com.ecommerce.user.dto.UserUpdateDTO;
import com.ecommerce.user.entity.UserStatus;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing user accounts, profiles, authentication, and user status")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Register a new user account with email, password, and profile information. Email must be unique in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with this email already exists"
            )
    })
    public ResponseEntity<UserResponseDTO> createUser(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve user profile and account information by user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with given ID"
            )
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id) {
        UserResponseDTO responseDTO = userService.getUserById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Get user by email",
            description = "Retrieve user profile and account information using email address. Useful for login and user lookup."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with given email"
            )
    })
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @Parameter(description = "User email address", example = "john.doe@example.com", required = true)
            @PathVariable String email) {
        UserResponseDTO responseDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all registered users in the system (Admin only in production)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of all users retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
            )
    )
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get users by status",
            description = "Retrieve all users filtered by account status (ACTIVE, INACTIVE, SUSPENDED, DELETED, etc.)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users filtered by status retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
            )
    )
    public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(
            @Parameter(description = "User account status", example = "ACTIVE", required = true)
            @PathVariable UserStatus status) {
        List<UserResponseDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user profile",
            description = "Update user account information including name, phone, address, and other profile details. Email cannot be changed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation errors"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with given ID"
            )
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user details", required = true)
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        UserResponseDTO responseDTO = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Permanently delete a user account from the system. This action cannot be undone. (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with given ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User cannot be deleted (has active orders or dependencies)"
            )
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update user account status",
            description = "Change user account status (e.g., ACTIVE, INACTIVE, SUSPENDED). Used for account activation, deactivation, or suspension."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User status updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status value"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with given ID"
            )
    })
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New account status", example = "ACTIVE", required = true)
            @RequestParam UserStatus status) {
        UserResponseDTO responseDTO = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }
}

/**


package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequestDTO;
import com.ecommerce.user.dto.UserResponseDTO;
import com.ecommerce.user.dto.UserUpdateDTO;
import com.ecommerce.user.entity.UserStatus;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO responseDTO = userService.getUserById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        UserResponseDTO responseDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(@PathVariable UserStatus status) {
        List<UserResponseDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        UserResponseDTO responseDTO = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        UserResponseDTO responseDTO = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }
}

 */