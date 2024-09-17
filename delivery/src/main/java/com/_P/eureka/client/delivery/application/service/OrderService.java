package com._P.eureka.client.delivery.application.service;

import com._P.eureka.client.delivery.application.dto.order.ReceiverDto;
import com._P.eureka.client.delivery.application.dto.order.RequestOrderDto;
import com._P.eureka.client.delivery.application.dto.order.ResponseOrderDto;
import com._P.eureka.client.delivery.client.ObjectClient;
import com._P.eureka.client.delivery.doamin.common.DeliveryRoleEnum;
import com._P.eureka.client.delivery.doamin.model.Delivery;
import com._P.eureka.client.delivery.doamin.model.DeliveryRecord;
import com._P.eureka.client.delivery.doamin.model.Order;
import com._P.eureka.client.delivery.doamin.repository.DeliveryRecordRepository;
import com._P.eureka.client.delivery.doamin.repository.DeliveryRepository;
import com._P.eureka.client.delivery.doamin.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final DeliveryRepository deliveryRepository;
  private final DeliveryRecordRepository deliveryRecordRepository;
  private final ObjectClient objectClient;

  @Transactional
  public void create(RequestOrderDto request, ReceiverDto receiver) {

    // OrderDto 검증
    ResponseOrderDto response = objectClient.validate(request);

    System.out.println("@@@@@@response" + response.getSupplierHubId());
    System.out.println("@@@@@@response" + response.getRecipientAddress());

    // 주문 생성
    Order order = orderRepository.save(Order.builder()
            .supplierCompanyId(request.getSupplierCompanyId())
            .recipientCompanyId(request.getRecipientCompanyId())
            .productId(request.getProductId())
            .quantity(request.getQuantity())
            .recipientName(receiver.getRecipientName())
            .recipientAddress(response.getRecipientAddress())
            .recipientPhoneNumber(receiver.getRecipientPhoneNumber())
            .build()
    );

    // 배송 생성
    Delivery delivery = deliveryRepository.save(Delivery.builder()
            .orderId(order.getOrderId())
            .startHubId(response.getSupplierHubId())
            .endHubId(response.getRecipientHubId())
            .role(DeliveryRoleEnum.HUB_WAITING)
            .recipientAddress(response.getRecipientAddress())
            .recipientName(receiver.getRecipientName())
            .recipientPhone(receiver.getRecipientPhoneNumber())
            .build()
    );

    // 주문에 배송 생성된 배송 ID 넣어주기
    order.setDeliveryId(delivery.getDeliveryId());

    // 배송 경로 기록 생성
    deliveryRecordRepository.save(DeliveryRecord.builder()
            .delivery(delivery)
            .role(DeliveryRoleEnum.HUB_WAITING)
            // TODO 도전 기능
            // 예상 거리, 시간
            .build());


  }


}
