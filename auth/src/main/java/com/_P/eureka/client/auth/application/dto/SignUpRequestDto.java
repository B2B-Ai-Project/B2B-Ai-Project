package com._P.eureka.client.auth.application.dto;

import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원 가입 요청 객체
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    @Size(min=4,max=10)
    @Pattern(regexp="^(?=.*[a-z])(?=.*\\d).{4,10}$")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=8,max=15)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,15}$")
    private String password;

    @NotBlank
    @Size(min=13,max=13)
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}")
    private String phone;

    private UserRoleEnum role;

    private String token="";
}