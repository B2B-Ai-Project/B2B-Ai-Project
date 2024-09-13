package com._P.eureka.client.delivery.application.service;


import com._P.eureka.client.delivery.doamin.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
//  private final ObjectClient hubClient;

//  @Transactional
//  public OrderResDto create(CreateOrderDto orderResDto) {
//    // FeignClient 를 통하여 Delivery App (Order) -> Object App (Company)
//    hubClient.validateDto(orderResDto);
//
//  }
}
