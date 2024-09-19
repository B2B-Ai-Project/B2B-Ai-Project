package com._P.eureka.client.delivery.hub_route.dto;

import com._P.eureka.client.delivery.hub_route.entity.Hub_Route;
import com._P.eureka.client.delivery.hub_route.entity.Subpath;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SubpathCreateDto {
    private UUID hubRouteId;

    private UUID startHubId;

    private UUID endHubId;

    private int sequence;

    private int routeTime;

    public Subpath toEntity(Hub_Route hubRoute) {
        return Subpath.builder()
                .hubRoute(hubRoute)
                .startHubId(this.startHubId)
                .endHubId(this.endHubId)
                .sequence(this.sequence)
                .routeTime(this.routeTime)
                .build();
    }
}
