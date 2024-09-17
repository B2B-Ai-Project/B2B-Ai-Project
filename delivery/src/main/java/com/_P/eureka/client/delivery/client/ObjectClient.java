package com._P.eureka.client.delivery.client;


import com._P.eureka.client.delivery.application.dto.order.RequestOrderDto;
import com._P.eureka.client.delivery.application.dto.order.ResponseOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient("object-service")
public interface ObjectClient {
  // DeliveryPerson 에서 사용할 허브 존재 여부 검증 API
  @PostMapping("/api/hub/validate/{hubId}")
  UUID update(
          @PathVariable("hubId") UUID hubId
  );

  @PostMapping("/api/order/validate")
  ResponseOrderDto validate(
          @RequestBody RequestOrderDto request
  );

}
