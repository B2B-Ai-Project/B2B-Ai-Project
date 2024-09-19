package com._P.eureka.client.delivery.hub_route.dto;

import com._P.eureka.client.delivery.hub_route.entity.Hub_Route;
import com._P.eureka.client.delivery.hub_route.repository.HubRouteRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubRouteResponseDto {
    private UUID routeId;

    private UUID startHubId;

    private UUID endHubId;

    private List<SubpathResponseDto> subpaths;

    private boolean is_deleted;

    public HubRouteResponseDto(Hub_Route hubRoute){
        this.routeId = hubRoute.getRouteId();
        this.startHubId = hubRoute.getStartHubId();
        this.endHubId = hubRoute.getEndHubId();

    }
}

