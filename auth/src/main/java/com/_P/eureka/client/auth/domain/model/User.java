package com._P.eureka.client.auth.domain.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String username;

    // unique 제약조건 == 슬랙아이디
    @Email
    @Column(unique=true)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value=EnumType.STRING)
    private UserRoleEnum role;

    @Column(name="is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

    // user 객체 변환 메서드
    public static User create(String username,String email,String phone,String password,UserRoleEnum role){
        return User.builder()
                .username(username)
                .email(email)
                .phone(phone)
                .password(password)
                .role(role)
                .build();
    }
}
