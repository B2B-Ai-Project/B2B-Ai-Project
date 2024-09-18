package com._P.eureka.client.delivery.doamin.repository;

import com._P.eureka.client.delivery.doamin.model.Delivery;
import com._P.eureka.client.delivery.doamin.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
  Optional<Delivery> findByOrderId(UUID orderId);
  Page<Delivery> findAllByIsDeletedFalse(Pageable pageable);
}
