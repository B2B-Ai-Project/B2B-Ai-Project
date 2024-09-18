package com._P.eureka.client.delivery.application.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOrderDto {
  private UUID supplierHubId; // 공급 업체 허브 ID
  private UUID recipientHubId; // 수령 업체 허브 ID
  private String recipientAddress; // 배송지 주소

}
