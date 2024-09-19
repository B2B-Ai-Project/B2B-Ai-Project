package com._P.eureka.client.object.orderValidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrderDto {
  private UUID orderId;
  private UUID supplierCompanyId; // 공급 업체 ID
  private UUID recipientCompanyId; // 수령 업체 ID
  private UUID productId; // 제품 ID
  private Integer quantity; // 수량
}
