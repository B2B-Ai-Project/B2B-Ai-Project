package com._P.eureka.client.object.controller;

import com._P.eureka.client.object.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class HubController {
  private final HubService hubService;

  // DeliveryPerson 에서 사용할 허브 존재 여부 검증 API
  @PostMapping("/validate/{hubId}")
  public String validateHubId (
          @PathVariable UUID hubId
  ){
    return hubService.validateHubId(hubId);
  }
}
