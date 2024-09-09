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
  private UUID id;

  @Column(nullable = false)
  @Enumerated(value=EnumType.STRING)
  private DeliveryPersonRoleEnum role;
}
