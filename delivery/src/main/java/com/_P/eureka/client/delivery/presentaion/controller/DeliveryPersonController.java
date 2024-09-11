package com._P.eureka.client.delivery.presentaion.controller;

import com._P.eureka.client.delivery.application.dto.DeliveryPersonDto;
import com._P.eureka.client.delivery.application.dto.DeliveryPersonReqDto;
import com._P.eureka.client.delivery.application.dto.DeliveryPersonResDto;
import com._P.eureka.client.delivery.application.service.DeliveryPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveryPerson")
public class DeliveryPersonController {
  private final DeliveryPersonService deliveryPersonService;

  // 배송 담당자 생성
  @PostMapping("/create/{userId}")
  public DeliveryPersonResDto create(
          @PathVariable UUID userId
  ) {
    return deliveryPersonService.create(userId);
  }

  // 배송 담당자 내 정보 조회
  @GetMapping()
  public DeliveryPersonResDto getUser(
          @AuthenticationPrincipal
          CustomUserDetails userDetails
  ) {
    return deliveryPersonService.getUser(userDetails);
  }

  // 배송 당담자 권한 변경 및 허브ID 부여
  @PostMapping("/update/{hubId}")
  public DeliveryPersonResDto updateHub(
          @PathVariable UUID hubId,
          @AuthenticationPrincipal
          CustomUserDetails userDetails
  ) {
    // HubId 를 받아와서 배송 담당자에서 HubId 부여 및 권한 변경
    return deliveryPersonService.update(hubId, userDetails);
  }


}
