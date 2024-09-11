package com._P.eureka.client.auth.infrastructure.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { // passwordEncoder로 저장됨.
        return new BCryptPasswordEncoder();
        //  BCrypt : 비밀번호를 암호화해주는 Hash함수
    }
}