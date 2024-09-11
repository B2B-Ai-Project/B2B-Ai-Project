package com._P.eureka.client.auth.presentation.controller;

import com._P.eureka.client.auth.application.service.UserService;
import feign.Body;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  // 배송 담당자 생성 검증
  @PostMapping("/create/deliveryPerson/{userId}")
  public String createDeliveryPerson(
          @PathVariable UUID userId
  ){
    // 검증 완료 시 slackId 반환
    return userService.checkUser(userId);
  }



}
