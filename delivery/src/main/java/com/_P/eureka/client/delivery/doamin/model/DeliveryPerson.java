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
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID deliveryPersonId;

  private String hubId; // 업체 이동 담당자의 소속 허브

  private String email; // slackId

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private DeliveryPersonRoleEnum role;

  @Column(nullable = false)
  private boolean is_waiting = true; // 대기중인 배송 담당자

}

