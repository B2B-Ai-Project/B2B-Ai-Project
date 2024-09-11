package com._P.eureka.client.delivery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient("auth-service")
public interface AuthClient {
  @PostMapping("/create/deliveryPerson/{userId}")
  String create(
          @PathVariable("userId") UUID userId
  );

}

