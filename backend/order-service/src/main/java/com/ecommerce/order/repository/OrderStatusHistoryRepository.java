package com.ecommerce.order.repository;

import com.ecommerce.order.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrderId(Long orderId);

    @Query("SELECT osh FROM OrderStatusHistory osh WHERE osh.orderId = :orderId ORDER BY osh.createdAt DESC")
    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtDesc(@Param("orderId") Long orderId);

    @Query("SELECT osh FROM OrderStatusHistory osh WHERE osh.changedBy = :userId")
    List<OrderStatusHistory> findByChangedBy(@Param("userId") Long userId);
}