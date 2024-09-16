package com._P.eureka.client.delivery.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResDto {
  private String orderId;
  private String supplierId; // 공급 업체 ID
  private String recipientId; // 수령 업체 ID
  private String deliveryId; // 배송 ID
  private String productId; // 제품 ID
  private Integer quantity; // 수량


}
