package com._P.eureka.client.object.hub.repository;

import com._P.eureka.client.object.hub.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {
    Optional<Hub> findByName(String name);

    Optional<Hub> findByAddress(String address);

    Page<Hub> findByNameContaining(String searchValue, Pageable pageable);
    Page<Hub> findAllByIsDeletedFalse(Pageable pageable);
}