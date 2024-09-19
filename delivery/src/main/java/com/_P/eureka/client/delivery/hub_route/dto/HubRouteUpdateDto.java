package com._P.eureka.client.delivery.hub_route.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HubRouteUpdateDto {
    private UUID startHubId;

    private UUID endHubId;
}
