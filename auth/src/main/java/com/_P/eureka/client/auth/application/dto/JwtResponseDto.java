package com._P.eureka.client.auth.application.dto;

public class JwtResponseDto {

    private String token;

    public JwtResponseDto(String token) {
        this.token = token;
    }

    // getter
    public String getToken() {
        return token;
    }
}
