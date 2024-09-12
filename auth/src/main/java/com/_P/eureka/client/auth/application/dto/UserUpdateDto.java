package com._P.eureka.client.auth.application.dto;

import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserUpdateDto {

    private String username;
    private String password;
    private String email;
    private String phone;

}
