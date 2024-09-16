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
@Table(name = "p_delivery")
public class Delivery { // 배송
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID deliveryId;

  @Column(nullable = false)
  @Enumerated(value=EnumType.STRING)
  private DeliveryRoleEnum role;

  private String request;
  private String address;
  private String recipient;
  private String phone;

}
