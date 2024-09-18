package com._P.eureka.client.delivery.presentaion.controller;

import com._P.eureka.client.delivery.application.dto.deveryPerson.DeliveryPersonResDto;
import com._P.eureka.client.delivery.application.dto.order.CreateOrderDto;
import com._P.eureka.client.delivery.application.dto.order.OrderInfoDto;
import com._P.eureka.client.delivery.application.dto.order.ReceiverDto;
import com._P.eureka.client.delivery.application.dto.order.ResponseOrderDto;
import com._P.eureka.client.delivery.application.service.OrderService;
import com._P.eureka.client.delivery.doamin.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
  private final OrderService orderService;

  // 주문 생성
  @PostMapping("/create")
  public ResponseEntity<?> create(
          @RequestBody CreateOrderDto createOrderDto
  ) {
    orderService.create(createOrderDto.getRequest(), createOrderDto.getReceiver());
    return ResponseEntity.ok("주문 생성 완료");
  }

  // 주문 단건 조회 / 나중에 토큰으로 수정
  @GetMapping("/{orderId}")
  public OrderInfoDto getOrder(
          @PathVariable UUID orderId
          ){
    return orderService.getOrder(orderId);
  }

  // 주문 수정
  @PatchMapping("/{orderId}")
  public OrderInfoDto updateOrder(
          @PathVariable UUID orderId,
          @RequestBody ReceiverDto receiverDto
          ){
    return orderService.updateOrder(orderId, receiverDto);
  }

  // 주문 삭제 / 라이프 사이클이 같은 배송, 배송 경로 기록도 삭제해 주기
  @DeleteMapping("/{orderId}")
  public ResponseEntity<?> deleteOrder(
          @PathVariable UUID orderId
  ){
    orderService.deleteOrder(orderId);
    return ResponseEntity.ok("주문 삭제 완료");
  }

  @GetMapping("/search")
  public Page<OrderInfoDto> search(
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "size", defaultValue = "10") int size,
          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
          @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc // 오름차순
  ){
    return orderService.search(page - 1, size, sortBy, isAsc);
  }

}
