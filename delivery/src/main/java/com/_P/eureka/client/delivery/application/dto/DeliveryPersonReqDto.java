package com._P.eureka.client.delivery.application.dto;

import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import com._P.eureka.client.delivery.doamin.model.DeliveryPersonRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonReqDto {
  private UUID deliveryPersonId;
  private String hubId;
  private DeliveryPersonRoleEnum role;
}
