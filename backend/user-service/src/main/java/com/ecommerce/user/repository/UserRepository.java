package com.ecommerce.user.repository;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByStatus(UserStatus status);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}