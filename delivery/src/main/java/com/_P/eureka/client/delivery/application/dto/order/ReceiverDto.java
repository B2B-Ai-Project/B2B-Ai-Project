package com._P.eureka.client.delivery.application.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverDto {
  private String recipientName; // 수령인
  private String recipientPhoneNumber; // 수령인 전화번호

}
