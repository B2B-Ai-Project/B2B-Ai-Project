package com._P.eureka.client.object.hub.dto;

import com._P.eureka.client.object.hub.entity.Hub;
import lombok.Getter;
@Getter
public class HubCreateDto {
    private String name;

    private String address;

    private Long latitude;

    private Long longitude;

    public Hub toEntity(){
        return Hub.builder()
                .name(this.name)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
