package com._P.eureka.client.auth.domain.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "p_user")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String username;

    // unique 제약조건 == 슬랙아이디
    @Column(unique=true, nullable = false)
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




}
