package com._P.eureka.client.delivery.hub_route.repository;

import com._P.eureka.client.delivery.hub_route.dto.HubRouteResponseDto;
import com._P.eureka.client.delivery.hub_route.entity.Hub_Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<Hub_Route, UUID> {
    Page<Hub_Route> findByStartHubIdAndIsDeletedFalse(UUID startHubId, Pageable pageable);

    Page<Hub_Route> findByEndHubIdAndIsDeletedFalse(UUID startHubId, Pageable pageable);

    Page<Hub_Route> findByStartHubIdAndEndHubIdAndIsDeletedFalse(UUID startHubId, UUID endHubId, Pageable pageable);

    Page<Hub_Route> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Hub_Route> findByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);

    Optional<Hub_Route> findByRouteIdAndIsDeletedFalse(UUID hubRouteId);
}
