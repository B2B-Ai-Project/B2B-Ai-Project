package com._P.eureka.client.delivery.presentaion.controller;

import com._P.eureka.client.delivery.application.dto.delivery.DeliveryResponseDto;
import com._P.eureka.client.delivery.application.service.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
  private final DeliveryService deliveryService;

  // Create, Delete -> Order 와 생명주기가 같으므로 OrderService에서 처리

  // 배송 단건 조회
  @GetMapping("/{deliveryId}")
  public DeliveryResponseDto getDelivery(
          @PathVariable UUID deliveryId
  ){
      return deliveryService.getDelivery(deliveryId);
  }

  // 배송에 배송 담당자 지정
  @PatchMapping("/{deliveryId}/deliveryPerson/{deliveryPersonId}")
  public ResponseEntity<?> updateDeliveryPerson(
          @PathVariable UUID deliveryId,
          @PathVariable UUID deliveryPersonId
          ){
    try {
      deliveryService.updateDeliveryPerson(deliveryId, deliveryPersonId);
      return ResponseEntity.ok("배송 담당자 지정 성공");
    } catch (Exception e){
      // 에러 발생 시, 에러 메시지와 함께 400 Bad Request 반환
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/search")
  public Page<DeliveryResponseDto> search(
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "size", defaultValue = "10") int size,
          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
          @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc // 오름차순
  ){
    return deliveryService.search(page - 1, size, sortBy, isAsc);
  }


}
