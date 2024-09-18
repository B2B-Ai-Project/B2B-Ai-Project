package com._P.eureka.client.delivery.doamin.model;

import com._P.eureka.client.delivery.doamin.common.DeliveryRoleEnum;
import com._P.eureka.client.delivery.doamin.common.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Entity
@Table(name = "p_delivery")
public class Delivery extends TimeStamped { // 배송
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID deliveryId;

  private UUID orderId;
  private UUID startHubId; // 공급 허브 업체 ID = 출발 허브 ID
  private UUID endHubId; // 수령 허브 업체 ID = 목적지 허브 ID
  private UUID deliveryPersonId; // 배송 담당자

  @Column(nullable = false)
  @Enumerated(value=EnumType.STRING)
  private DeliveryRoleEnum role;

  private String recipientAddress;
  private String recipientName;
  private String recipientPhoneNumber;


}
