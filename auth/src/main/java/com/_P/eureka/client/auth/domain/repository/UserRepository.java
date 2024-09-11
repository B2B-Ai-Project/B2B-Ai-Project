package com._P.eureka.client.auth.domain.repository;

import com._P.eureka.client.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Object> findByUsername(String username);
    Optional<Object> findByEmail(String email);
}
