package com._P.eureka.client.delivery.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient("object-service")
public interface ObjectClient {
  @PostMapping("/api/hub/validate/{hubId}")
  UUID update(
          @PathVariable("hubId") UUID hubId
  );
}
