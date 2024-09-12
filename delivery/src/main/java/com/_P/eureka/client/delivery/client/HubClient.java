package com._P.eureka.client.delivery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient("hub-service")
public interface HubClient {
  @PostMapping("/validate/{hubId}")
  String update(
          @PathVariable("hubId") UUID HubId
  );
}
