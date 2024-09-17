package com._P.eureka.client.delivery.application.dto.order;

import lombok.Getter;

@Getter
public class CreateOrderDto {
  private RequestOrderDto request;
  private ReceiverDto receiver;
}
