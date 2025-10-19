package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.PaymentTransaction;
import com.ecommerce.payment.entity.TransactionStatus;
import com.ecommerce.payment.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    Optional<PaymentTransaction> findByTransactionId(String transactionId);

    List<PaymentTransaction> findByPaymentId(Long paymentId);

    List<PaymentTransaction> findByTransactionType(TransactionType transactionType);

    List<PaymentTransaction> findByStatus(TransactionStatus status);

    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.payment.id = :paymentId ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByPaymentIdOrderByCreatedAtDesc(@Param("paymentId") Long paymentId);

    boolean existsByTransactionId(String transactionId);
}