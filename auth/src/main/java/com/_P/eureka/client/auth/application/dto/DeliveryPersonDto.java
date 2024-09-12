package com._P.eureka.client.auth.application.dto;

import com._P.eureka.client.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Create DeliveryPerson Request Dto
public class DeliveryPersonDto {
  private UUID userId;
  private String email;

  public static DeliveryPersonDto fromEntity(User user){
    return DeliveryPersonDto.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .build();
  }
}
