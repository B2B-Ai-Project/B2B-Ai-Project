package com._P.eureka.client.auth.application.service;

import com._P.eureka.client.auth.application.dto.LoginRequestDto;
import com._P.eureka.client.auth.application.dto.SignUpRequestDto;
import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.repository.UserRepository;
import com._P.eureka.client.auth.infrastructure.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 회원 존재 여부 검증
    public boolean verifyUser(final String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // 회원가입
    public void signUp(SignUpRequestDto requestDto) {

        String username = requestDto.getUsername();
        Optional<User> checkUsername = userRepository.findByUsername(username);

        // 비밀번호 검증 (공백유효성검사)
        if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
        }
        // 중복검사
        if (checkUsername.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"중복된 사용자입니다.");
        }

        String email = requestDto.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"중복된 이메일 주소입니다.");
        }

        String phone = requestDto.getPhone();

        String password = passwordEncoder.encode(requestDto.getPassword());


        User user = User.create(username, email, phone, password, requestDto.getRole());



        userRepository.save(user);
    }

//    // 로그인
//    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
//        // 사용자 인증 처리 (DB에서 사용자 정보 조회)
//        User user = userRepository.findByUsername(requestDto.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
//        // 비밀번호 검증
//        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//        // JWT 토큰 생성
//        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
//
//        // 토큰 로그로 확인
//        log.info("JWT Token: {}", token);
//        // JWT 토큰 반환
//        return token;
//    }

//    public void logout() {
//
//    }
}
