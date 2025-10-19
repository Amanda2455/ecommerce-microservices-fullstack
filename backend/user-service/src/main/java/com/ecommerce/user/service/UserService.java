package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequestDTO;
import com.ecommerce.user.dto.UserResponseDTO;
import com.ecommerce.user.dto.UserUpdateDTO;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserStatus;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.exception.UserAlreadyExistsException;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        // Check if user already exists
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + requestDTO.getEmail() + " already exists");
        }

        if (requestDTO.getPhoneNumber() != null &&
                userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
            throw new UserAlreadyExistsException("User with phone number " + requestDTO.getPhoneNumber() + " already exists");
        }

        User user = new User();
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword()); // In real scenario, encode this
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        user.setAddress(requestDTO.getAddress());
        user.setCity(requestDTO.getCity());
        user.setState(requestDTO.getState());
        user.setCountry(requestDTO.getCountry());
        user.setZipCode(requestDTO.getZipCode());
        user.setRole(requestDTO.getRole() != null ? requestDTO.getRole() : user.getRole());

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponseDTO(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (updateDTO.getFirstName() != null) {
            user.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            user.setLastName(updateDTO.getLastName());
        }
        if (updateDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateDTO.getPhoneNumber());
        }
        if (updateDTO.getAddress() != null) {
            user.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getCity() != null) {
            user.setCity(updateDTO.getCity());
        }
        if (updateDTO.getState() != null) {
            user.setState(updateDTO.getState());
        }
        if (updateDTO.getCountry() != null) {
            user.setCountry(updateDTO.getCountry());
        }
        if (updateDTO.getZipCode() != null) {
            user.setZipCode(updateDTO.getZipCode());
        }

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setCountry(user.getCountry());
        dto.setZipCode(user.getZipCode());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}