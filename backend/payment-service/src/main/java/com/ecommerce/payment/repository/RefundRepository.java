package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.Refund;
import com.ecommerce.payment.entity.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByRefundId(String refundId);

    List<Refund> findByPaymentId(Long paymentId);

    List<Refund> findByOrderId(Long orderId);

    List<Refund> findByStatus(RefundStatus status);

    @Query("SELECT r FROM Refund r WHERE r.orderId = :orderId ORDER BY r.createdAt DESC")
    List<Refund> findByOrderIdOrderByCreatedAtDesc(@Param("orderId") Long orderId);

    boolean existsByRefundId(String refundId);
}