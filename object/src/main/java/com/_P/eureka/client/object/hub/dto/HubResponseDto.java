package com._P.eureka.client.object.hub.dto;

import com._P.eureka.client.object.hub.entity.Hub;

public class HubResponseDto {
    private String hubId;
    private String name;

    private String address;

    private int latitude;

    private int longitude;

    public HubResponseDto(Hub hub){
        this.hubId = hub.getHubId();
        this.name = hub.getName();
        this.address = hub.getAddress();
        this.latitude = hub.getLatitude();
        this.longitude = hub.getLongitude();
    }
}
