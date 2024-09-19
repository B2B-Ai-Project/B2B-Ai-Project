package com._P.eureka.client.object.orderValidate.controller;

import com._P.eureka.client.object.orderValidate.dto.RequestOrderDto;
import com._P.eureka.client.object.orderValidate.dto.ResponseOrderDto;
import com._P.eureka.client.object.orderValidate.service.OrderValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderValidateController {
  private final OrderValidateService orderValidateService;

  // OrderDto 검증
  @PostMapping("/validate")
  public ResponseOrderDto orderValidate(
          @RequestBody RequestOrderDto request
  ) {
    return orderValidateService.orderValidate(request);
  }

}
