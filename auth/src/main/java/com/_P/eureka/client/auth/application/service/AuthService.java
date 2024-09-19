package com._P.eureka.client.auth.application.service;

import com._P.eureka.client.auth.application.dto.LoginRequestDto;
import com._P.eureka.client.auth.application.dto.SignUpRequestDto;
import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import com._P.eureka.client.auth.domain.repository.UserRepository;
import com._P.eureka.client.auth.infrastructure.jwt.JwtUtil;
import com._P.eureka.client.auth.infrastructure.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
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

    public String login(LoginRequestDto loginRequest) {
        try {
            // Spring Security 인증 처리
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 인증 성공 시, JWT 토큰 생성
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String token = jwtUtil.createToken(userDetails.getUsername(), userDetails.getUser().getRole());

            return token;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // 로그아웃 토큰을 redis에 블랙리스트에 저장
    public void logout(String token) {
        // JWT 토큰에서 만료 시간 확인
        long expirationTime = jwtUtil.getExpiration(token);

        // Redis에 블랙리스트로 저장 (토큰을 키로 사용하고, 만료 시간까지 유지)
        redisTemplate.opsForValue().set(token, "logout", expirationTime, TimeUnit.MILLISECONDS);
    }



    // JWT 검증 로직
    public boolean validateToken(String token) {
        try {
            Key key = getKeyFromToken(token); // Log key extraction success/failure
            if (key == null) {
                log.error("Signing key extraction failed.");
                return false;
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("Token validation successful");
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: " + e.getMessage(), e); // Log the exception
            return false;
        }
    }

    // JWT에서 역할에 맞는 서명 키를 반환하는 메서드
    private Key getKeyFromToken(String token) {
        try {
            log.info("Extracting claims from token: {}", token);  // 토큰 확인

            Claims claims = Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            UserRoleEnum role = UserRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
                            log.info("Extracted role from token: {}", role);
                            return jwtUtil.getKeyByRole(role); // 역할에 맞는 서명 키 반환
                        }
                    })
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            UserRoleEnum role = UserRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
            return jwtUtil.getKeyByRole(role); // 역할에 맞는 서명 키 반환

        } catch (Exception e) {
            log.error("Error extracting key from token: " + e.getMessage(), e);
            throw new RuntimeException("Failed to extract key from token", e);
        }
    }

    // 토큰이 블랙리스트에 있는지 확인하는 로직 -- 수정요구
    public boolean isTokenBlacklisted(String token) {
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate is not initialized.");
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(token)); // Key 존재 여부만 확인
    }
}
