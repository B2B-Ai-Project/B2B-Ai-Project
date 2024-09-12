package com._P.eureka.client.delivery.doamin.repository;

import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UUID> {
}
