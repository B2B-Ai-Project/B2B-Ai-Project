package com._P.eureka.client.delivery.doamin.repository;

import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import com._P.eureka.client.delivery.doamin.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UUID> {
  Page<DeliveryPerson> findAllByIsDeletedFalse(Pageable pageable);
}
