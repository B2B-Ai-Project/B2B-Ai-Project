package com._P.eureka.client.auth.presentation.controller;


import com._P.eureka.client.auth.application.dto.SignUpRequestDto;
import com._P.eureka.client.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

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

//     // 로그인
//    @PostMapping("/login")
//    @Operation(summary = "로그인 API", description = "로그인, 토큰값을 반환합니다.")
//    public ResponseEntity<String> login(
//            @RequestBody LoginRequestDto requestDto, HttpServletResponse res){
//
//         String token = authService.login(requestDto, res);
//
//        // 토큰 로그로 확인
//        log.info("Generated Token: {}", token);
//
//         return ResponseEntity.ok(token);
//    }

//    // 로그아웃
//    @PostMapping("/logout")
//    @Operation(summary = "로그아웃 API", description = "User를 로그아웃합니다.")
//    public ResponseEntity<String> logout(){
//         authService.logout();
//         return ResponseEntity.ok("로그아웃이 완료되었습니다.");
//
//    }

}
