package com._P.eureka.client.delivery.hub_route.repository;

import com._P.eureka.client.delivery.hub_route.entity.Hub_Route;
import com._P.eureka.client.delivery.hub_route.entity.Subpath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubpathRepository extends JpaRepository<Subpath, UUID> {

    boolean existsByHubRouteAndStartHubIdAndEndHubIdAndIsDeletedFalse(Hub_Route hubRoute, UUID startHubId, UUID endHubId);
}
