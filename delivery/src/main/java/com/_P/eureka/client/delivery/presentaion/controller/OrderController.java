package com._P.eureka.client.delivery.presentaion.controller;

import com._P.eureka.client.delivery.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
  private final OrderService orderService;

//  @PostMapping("create")
//  public OrderResDto create(
//          @RequestBody CreateOrderDto orderResDto
//  ){
//      return orderService.create(orderResDto);
//  }



}
