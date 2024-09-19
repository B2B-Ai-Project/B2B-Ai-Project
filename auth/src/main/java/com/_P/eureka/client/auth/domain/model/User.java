package com._P.eureka.client.auth.domain.model;


import com._P.eureka.client.auth.application.dto.UserUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id", updatable = false, nullable = false)
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

    // 회원가입시 수동등록
    @Column(name = "create_by")
    private String createdBy;
    @Column(name = "update_by")
    private String updatedBy;

    // 회원 create
    public static User create(String username,String email,String phone,String password,UserRoleEnum role){
        return User.builder()
                .username(username)
                .email(email)
                .phone(phone)
                .password(password)
                .role(role)
                .createdBy(username)
                .updatedBy(username)
                .build();
    }


    // 회원 update
    public User updateUser(UserUpdateDto requestDto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .userId(this.userId) // 기존 데이터 유지
                .username(requestDto.getUsername() != null ? requestDto.getUsername() : this.username)
                .email(requestDto.getEmail() != null ? requestDto.getEmail() : this.email)
                .phone(requestDto.getPhone() != null ? requestDto.getPhone() : this.phone)
                .password(requestDto.getPassword() != null ? passwordEncoder.encode(requestDto.getPassword()) : this.password)
                .role(this.role) // 기존 역할 유지
                .createdBy(this.createdBy) // 기존 생성자 유지
                .updatedBy(this.username) // 업데이트한 사용자를 현재 사용자로 설정
                .build();
    }



}
