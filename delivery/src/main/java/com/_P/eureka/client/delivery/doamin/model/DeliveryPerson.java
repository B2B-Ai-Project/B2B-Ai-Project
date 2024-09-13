package com._P.eureka.client.delivery.doamin.model;

import com._P.eureka.client.delivery.doamin.common.DeliveryPersonRoleEnum;
import com._P.eureka.client.delivery.doamin.common.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_delivery_person")
public class DeliveryPerson extends TimeStamped { // 배송 담당자
  @Id
  private UUID deliveryPersonId;

  private UUID hubId; // 업체 이동 담당자의 소속 허브

  @Column(unique=true)
  private String email; // slackId

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private DeliveryPersonRoleEnum role;

  @Column(nullable = false)
  private boolean is_waiting = true; // 대기중인 배송 담당자


  // [요구사항] - 배송 담당자의 ID는 사용자 관리 엔티티의 사용자와 동일해야 합니다.
  public void updateDeliveryPersonId(UUID deliveryPersonId) {
    this.deliveryPersonId = deliveryPersonId;
  }

  public void updateHubAndRole(UUID validateHubId, DeliveryPersonRoleEnum recipient) {
    this.hubId = validateHubId;
    this.role = recipient;
  }
}

