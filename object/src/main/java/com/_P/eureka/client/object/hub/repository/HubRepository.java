package com._P.eureka.client.object.hub.repository;

import com._P.eureka.client.object.hub.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub,String> {
    Optional findByName(String name);

    Optional findByAddress(String address);

    Page<Hub> findByNameContaining(String searchValue, Pageable pageable);
    Page<Hub> findAllByIsDeletedFalse(Pageable pageable);
}
