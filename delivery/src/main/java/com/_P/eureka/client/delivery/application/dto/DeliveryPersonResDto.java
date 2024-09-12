package com._P.eureka.client.delivery.application.dto;

import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonResDto {
  private UUID deliveryPersonId;
  private String hubId;
  private String email;
  private String role;
  private boolean is_waiting;
  private boolean is_deleted;

  public static DeliveryPersonResDto fromEntity(DeliveryPerson user) {
    return DeliveryPersonResDto.builder()
            .deliveryPersonId(user.getDeliveryPersonId())
            .hubId(user.getHubId())
            .email(user.getEmail())
            .role(String.valueOf(user.getRole()))
            .is_waiting(user.is_waiting())
            .build();
  }

  public DeliveryPersonResDto(DeliveryPerson deliveryPerson) {
    this.deliveryPersonId = deliveryPerson.getDeliveryPersonId();
    this.hubId = deliveryPerson.getHubId();
    this.email = deliveryPerson.getEmail();
    this.role = String.valueOf(deliveryPerson.getRole());
    this.is_waiting = deliveryPerson.is_waiting();
    this.is_deleted = deliveryPerson.is_deleted();
  }

}
