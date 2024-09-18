package com._P.eureka.client.delivery.application.dto.delivery;

import com._P.eureka.client.delivery.doamin.common.DeliveryRoleEnum;
import com._P.eureka.client.delivery.doamin.model.Delivery;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
  private UUID deliveryId;
  private UUID orderId;
  private UUID startHubId; // 공급 허브 업체 ID = 출발 허브 ID
  private UUID endHubId; // 수령 허브 업체 ID = 목적지 허브 ID
  private UUID deliveryPersonId; // 배송 담당자
  private DeliveryRoleEnum role;
  private String recipientAddress;
  private String recipientName;
  private String recipientPhoneNumber;

  public static DeliveryResponseDto fromEntity(Delivery delivery){
    return DeliveryResponseDto.builder()
            .deliveryId(delivery.getDeliveryId())
            .orderId(delivery.getOrderId())
            .startHubId(delivery.getStartHubId())
            .endHubId(delivery.getEndHubId())
            .deliveryPersonId(delivery.getDeliveryPersonId())
            .role(delivery.getRole())
            .recipientAddress(delivery.getRecipientAddress())
            .recipientName(delivery.getRecipientName())
            .recipientPhoneNumber(delivery.getRecipientPhoneNumber())
            .build();
  }

}
