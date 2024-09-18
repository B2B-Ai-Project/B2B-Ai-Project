package com._P.eureka.client.delivery.application.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {
  private RequestOrderDto request;
  private ReceiverDto receiver;
}
