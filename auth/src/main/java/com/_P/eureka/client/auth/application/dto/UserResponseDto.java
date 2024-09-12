package com._P.eureka.client.auth.application.dto;

import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private UUID userId;
    private String username;
    private String password;
    private String email;
    private UserRoleEnum role;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
