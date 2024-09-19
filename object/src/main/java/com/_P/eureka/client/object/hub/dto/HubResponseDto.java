package com._P.eureka.client.object.hub.dto;

import com._P.eureka.client.object.hub.entity.Hub;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubResponseDto {
    private UUID hubId;
    private String name;

    private String address;

    private Long latitude;

    private Long longitude;

    public HubResponseDto(Hub hub){
        this.hubId = hub.getHubId();
        this.name = hub.getName();
        this.address = hub.getAddress();
        this.latitude = hub.getLatitude();
        this.longitude = hub.getLongitude();
    }
}
