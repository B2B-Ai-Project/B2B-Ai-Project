package com._P.eureka.client.object.repository;

import com._P.eureka.client.object.hub.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {
}
