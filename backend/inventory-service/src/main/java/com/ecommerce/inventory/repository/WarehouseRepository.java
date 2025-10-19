package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.entity.WarehouseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCode(String code);

    List<Warehouse> findByStatus(WarehouseStatus status);

    List<Warehouse> findByCity(String city);

    boolean existsByCode(String code);
}