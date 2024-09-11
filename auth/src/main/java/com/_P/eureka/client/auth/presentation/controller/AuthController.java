package com._P.eureka.client.auth.presentation.controller;


import com._P.eureka.client.auth.application.service.AuthService;
import com._P.eureka.client.auth.application.dto.SignUpRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
     @PostMapping("/sign-up")
     @Operation(summary = "회원 가입 API", description = "User을 생성, 등록합니다.")
     public ResponseEntity<String> signUp(
             @RequestBody @Valid SignUpRequestDto requestDto){
             authService.signUp(requestDto);
             return  ResponseEntity.ok("회원가입이 되었습니다.");
     }

    // 로그인

    // 로그아웃

}
