package com._P.eureka.client.delivery.dto;

// object에서의 Hub 값 읽어오기 위함

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

}