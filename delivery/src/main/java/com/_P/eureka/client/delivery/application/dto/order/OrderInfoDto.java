package com._P.eureka.client.delivery.application.dto.order;

import com._P.eureka.client.delivery.doamin.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDto {
  private UUID orderId;
  private UUID supplierCompanyId; // 공급 허브 업체 ID
  private UUID recipientCompanyId; // 수령 허브 업체 ID
  private UUID deliveryId; // 배송 ID
  private UUID productId; // 제품 ID
  private Integer quantity; // 주문 수량
  private String recipientName; // 수령인
  private String recipientAddress; // 배송지 주소
  private String recipientPhoneNumber; // 수령인 전화번호

  public static OrderInfoDto fromEntity(Order order) {
    return OrderInfoDto.builder()
            .orderId(order.getOrderId())
            .supplierCompanyId(order.getSupplierCompanyId())
            .recipientCompanyId(order.getRecipientCompanyId())
            .deliveryId(order.getDeliveryId())
            .productId(order.getProductId())
            .quantity(order.getQuantity())
            .recipientName(order.getRecipientName())
            .recipientAddress(order.getRecipientAddress())
            .recipientPhoneNumber(order.getRecipientPhoneNumber())
            .build();
  }

  public OrderInfoDto(Order order) {
    this.orderId = order.getOrderId();
    this.supplierCompanyId = order.getSupplierCompanyId();
    this.recipientCompanyId = order.getRecipientCompanyId();
    this.deliveryId = order.getDeliveryId();
    this.productId = order.getProductId();
    this.quantity = order.getQuantity();
    this.recipientName = order.getRecipientName();
    this.recipientAddress = order.getRecipientAddress();
    this.recipientPhoneNumber = order.getRecipientPhoneNumber();
  }

}
