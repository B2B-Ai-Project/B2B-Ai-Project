package com._P.eureka.client.delivery.hub_route.dto;

import com._P.eureka.client.delivery.hub_route.entity.Subpath;

import java.util.UUID;

public class SubpathResponseDto {

    private UUID subPathId;
    private UUID hubRouteId;

    private UUID startHubId;

    private UUID endHubId;

    private int sequence;

    private int routeTime;

    public SubpathResponseDto(Subpath subpath){
        this.subPathId = subpath.getSubPathId();
        this.hubRouteId = subpath.getSubPathId();
        this.startHubId = subpath.getStartHubId();
        this.endHubId = subpath.getEndHubId();
        this.sequence = subpath.getSequence();
        this.routeTime = subpath.getRouteTime();
    }
}
