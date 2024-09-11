package com._P.eureka.client.auth.domain.repository;

import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.infrastructure.repository.JpaUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
