package com._P.eureka.client.delivery.doamin.model;

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
@Table(name = "p_order")
public class Order extends TimeStamped { // 주문
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID orderId;

  private UUID supplierCompanyId; // 공급 허브 업체 ID
  private UUID recipientCompanyId; // 수령 허브 업체 ID
  private UUID deliveryId; // 배송 ID
  private UUID productId; // 제품 ID
  private Integer quantity; // 주문 수량

  private String recipientName; // 수령인
  private String recipientAddress; // 배송지 주소
  private String recipientPhoneNumber; // 수령인 전화번호
}
