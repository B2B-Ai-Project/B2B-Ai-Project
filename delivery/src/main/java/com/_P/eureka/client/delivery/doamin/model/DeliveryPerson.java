package com._P.eureka.client.delivery.doamin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_delivery_person")
public class DeliveryPerson { // 배송 담당자
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID deliveryPersonId;

  private String hubId; // 업체 이동 담당자의 소속 허브

  private String email; // slackId

  @Column(nullable = false)
  @Enumerated(value=EnumType.STRING)
  private DeliveryPersonRoleEnum role;

  @Column(nullable = false)
  private boolean is_waiting = true; // 대기중인 배송 담당자

  @Column(nullable = false)
  private boolean is_deleted = false;

  public void updateHubAndRole(String hubId, DeliveryPersonRoleEnum role) {
    this.hubId = hubId;
    this.role = role;
  }



}
