package com._P.eureka.client.auth.application.service;

import com._P.eureka.client.auth.application.dto.SignUpRequestDto;
import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 존재 여부 검증
    public Boolean verifyUser(final String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void signUp(SignUpRequestDto requestDto) {

        String username = requestDto.getUsername();
        Optional<Object> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자입니다.");
        }

        String email = requestDto.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("중복된 이메일 주소입니다.");
        }

        String phone = requestDto.getPhone();

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = User.create(username, email, phone, password, requestDto.getRole());
        userRepository.save(user);
    }

}
