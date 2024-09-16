package com._P.eureka.client.delivery.doamin.model;

import com._P.eureka.client.delivery.doamin.common.DeliveryRoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_delivery_record")
public class DeliveryRecord { // 배송 경로 기록
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID deliveryRecordId;

  @Column(nullable = false)
  @Enumerated(value=EnumType.STRING)
  private DeliveryRoleEnum role;

  private Double estimatedDistance; // 예상 거리
  private Integer estimatedTime; // 예상 시간
  private Double actualDistance; // 실제 거리
  private Integer actualTime; // 실제 시간
}
