package com._P.eureka.client.delivery.doamin.repository;

import com._P.eureka.client.delivery.doamin.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
