package com._P.eureka.client.auth.presentation.controller;


import com._P.eureka.client.auth.application.dto.JwtResponseDto;
import com._P.eureka.client.auth.application.dto.LoginRequestDto;
import com._P.eureka.client.auth.application.dto.SignUpRequestDto;
import com._P.eureka.client.auth.application.service.AuthService;
import com._P.eureka.client.auth.infrastructure.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    // 회원가입
     @PostMapping("/sign-up")
     @Operation(summary = "회원가입 API", description = "User을 생성, 등록합니다.")
     public ResponseEntity<String> signUp(
             @RequestBody @Valid SignUpRequestDto requestDto, BindingResult bindingResult){
            // @Valid Dto 검증
             if (bindingResult.hasErrors()) {
                 String message = bindingResult.getFieldError().getDefaultMessage();
                 log.error(message);
                 // 400 Bad Request와 함께 오류 메시지 반환
                 return ResponseEntity.badRequest().body(message);
             }
             authService.signUp(requestDto);
             return  ResponseEntity.ok("회원가입이 되었습니다.");
     }


    // 로그인 API
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "로그인, 토큰값을 반환합니다.")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }


    // 로그아웃 API
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // "Bearer " 부분 제거
        token = token.substring(7);

        // 로그아웃 로직 호출 (토큰을 블랙리스트에 추가)
        authService.logout(token);
        return ResponseEntity.ok("로그아웃 성공");
    }


    // 블랙리스트 확인 API
    @GetMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        // "Bearer " 부분 제거
        token = token.substring(7);

        // 토큰이 블랙리스트에 있는지 확인
        if (authService.isTokenBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is blacklisted");
        }

        // JWT 검증
        if (!authService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        return ResponseEntity.ok("Token is valid");
    }

}
