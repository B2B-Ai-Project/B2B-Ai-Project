package com._P.eureka.client.delivery.presentaion.controller;

import com._P.eureka.client.delivery.application.dto.deveryPerson.DeliveryPersonResDto;
import com._P.eureka.client.delivery.application.service.DeliveryPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveryPerson")
public class DeliveryPersonController {
  private final DeliveryPersonService deliveryPersonService;

  @GetMapping("/test")
  public String test(){
    return "테스트 성공";
  }


  //배송 담당자 생성
  @PostMapping("/create/{userId}")
  public DeliveryPersonResDto create(
          @PathVariable UUID userId
  ) {
    return deliveryPersonService.create(userId);
  }

  // 배송 담당자 내 정보 조회 / 토큰 사용 방법으로 리팩토링
  @GetMapping("/get/{userId}")
  public DeliveryPersonResDto getUser(
          @PathVariable UUID userId
  ) {
    return deliveryPersonService.getUser(userId);
  }

  // 배송 당담자 권한 변경 및 허브ID 부여 / 토큰 사용 방법으로 리팩토링
  @PostMapping("/update/{userId}/hub/{hubId}")
  public DeliveryPersonResDto updateHub(
          @PathVariable UUID userId,
          @PathVariable UUID hubId
  ) {
    // HubId 를 받아와서 배송 담당자에서 HubId 부여 및 권한 변경
    return deliveryPersonService.update(userId, hubId);
  }

  @GetMapping("/search")
  public Page<DeliveryPersonResDto> search(
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "size", defaultValue = "10") int size,
          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
          @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc // 오름차순
  ){
    return deliveryPersonService.search(page - 1, size, sortBy, isAsc);
  }



}
