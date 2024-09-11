package com._P.eureka.client.object.service;

import com._P.eureka.client.object.hub.Hub;
import com._P.eureka.client.object.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {
  private final HubRepository hubRepository;

  // DeliveryPerson 에서 사용할 허브 존재 여부 검증 API
  public String validateHubId(UUID hubId) {
    Hub hub = hubRepository.findById(hubId).orElseThrow(()->
            new IllegalArgumentException("허브가 존재하지 않습니다."));
    return hub.getHub_id();
  }
}
