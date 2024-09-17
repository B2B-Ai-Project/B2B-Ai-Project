package com._P.eureka.client.delivery.presentaion.controller;

import com._P.eureka.client.delivery.application.dto.order.CreateOrderDto;
import com._P.eureka.client.delivery.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/create")
  public void create(
          @RequestBody CreateOrderDto createOrderDto
  ) {
    orderService.create(createOrderDto.getRequest(), createOrderDto.getReceiver());

  }

}
