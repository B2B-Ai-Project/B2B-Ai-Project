package com._P.eureka.client.delivery.hub_route.dto;

import com._P.eureka.client.delivery.hub_route.entity.Hub_Route;
import com._P.eureka.client.delivery.hub_route.entity.Subpath;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class HubRouteCreateDto {
    private UUID startHubId;

    private UUID endHubId;

    public Hub_Route toEntity(List<Subpath> subpathList){
        return Hub_Route.builder()
                .startHubId(this.startHubId)
                .endHubId(this.endHubId)
                .subpaths(subpathList)
                .build();
    }
}
