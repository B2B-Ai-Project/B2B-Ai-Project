package com._P.eureka.client.delivery.application.service;

import com._P.eureka.client.delivery.application.dto.deveryPerson.DeliveryPersonResDto;
import com._P.eureka.client.delivery.application.dto.order.OrderInfoDto;
import com._P.eureka.client.delivery.application.dto.order.ReceiverDto;
import com._P.eureka.client.delivery.application.dto.order.RequestOrderDto;
import com._P.eureka.client.delivery.application.dto.order.ResponseOrderDto;
import com._P.eureka.client.delivery.client.ObjectClient;
import com._P.eureka.client.delivery.doamin.common.DeliveryRoleEnum;
import com._P.eureka.client.delivery.doamin.model.Delivery;
import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import com._P.eureka.client.delivery.doamin.model.DeliveryRecord;
import com._P.eureka.client.delivery.doamin.model.Order;
import com._P.eureka.client.delivery.doamin.repository.DeliveryRecordRepository;
import com._P.eureka.client.delivery.doamin.repository.DeliveryRepository;
import com._P.eureka.client.delivery.doamin.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final DeliveryRepository deliveryRepository;
  private final DeliveryRecordRepository deliveryRecordRepository;
  private final ObjectClient objectClient;

  // 주문 생성
  @Transactional
  public void create(RequestOrderDto request, ReceiverDto receiver) {

    // OrderDto 검증
    ResponseOrderDto response = objectClient.validate(request);

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
            .recipientPhoneNumber(receiver.getRecipientPhoneNumber())
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

  // 주문 단건 조회
  public OrderInfoDto getOrder(UUID orderId) {
    Order order = findById(orderId);
    return OrderInfoDto.fromEntity(order);
  }

  // 주문 수정
  @Transactional
  public OrderInfoDto updateOrder(UUID orderId, ReceiverDto receiverDto) {
    // 주문 수정
    Order order = findById(orderId);
    order.setRecipientName(receiverDto.getRecipientName());
    order.setRecipientPhoneNumber(receiverDto.getRecipientPhoneNumber());

    // 배송 수정
    Delivery delivery = findByOrderId(order.getOrderId());
    delivery.setRecipientName(receiverDto.getRecipientName());
    delivery.setRecipientPhoneNumber(receiverDto.getRecipientPhoneNumber());

    return OrderInfoDto.fromEntity(order);
  }

  // 주문 삭제 / 라이프 사이클이 같은 배송, 배송 경로 기록도 삭제해 주기
  @Transactional
  public void deleteOrder(UUID orderId) {
    Order order = findById(orderId);
    Delivery delivery = findByOrderId(order.getOrderId());

    DeliveryRecord deliveryRecord = deliveryRecordRepository.findByDelivery_DeliveryId(delivery.getDeliveryId()).orElseThrow(()->
            new IllegalArgumentException("배송에 해당하는 배송 경로 기록이 없습니다."));

    // 배송의 상태가 대기중일 경우에만 삭제 가능
    if (delivery.getRole().equals(DeliveryRoleEnum.HUB_WAITING)){
      // "주문 -> 배송 -> 배송 경로 기록" 은 같은 라이프 사이클을 가진다.
      // 주문 취소 시 해당 제품에 주문 수량 반환
      objectClient.returnQuantity(order.getProductId(), order.getQuantity());
      order.setDeleted(true);
      delivery.setDeleted(true);
      deliveryRecord.setDeleted(true);
    }else {
      throw new IllegalArgumentException("배송이 이미 진행되었습니다.");
    }
  }

  // 주문에 해당하는 배송 조회
  public Delivery findByOrderId(UUID orderId){
    return deliveryRepository.findByOrderId(orderId).orElseThrow(()->
            new IllegalArgumentException("주문에 해당하는 배송이 없습니다"));
  }


  public Page<OrderInfoDto> search(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC; // 오름차순 or 내림차순
    Sort sort = Sort.by(direction, sortBy); // 정렬 방향으로 Sort 객체 생성
    Pageable pageable = PageRequest.of(page, size, sort);

    // is_deleted가 false인 Order 목록만 조회
    Page<Order> orderPage = orderRepository.findAllByIsDeletedFalse(pageable);

    return orderPage.map(OrderInfoDto::new);
  }

  public Order findById(UUID orderId){
    Order order = orderRepository.findById(orderId).orElseThrow(()->
            new IllegalArgumentException("주문이 존재하지 않습니다."));
    if (order.isDeleted()){
      throw new IllegalArgumentException("이미 삭제 요청된 주문입니다.");
    }
    return order;
  }
}
