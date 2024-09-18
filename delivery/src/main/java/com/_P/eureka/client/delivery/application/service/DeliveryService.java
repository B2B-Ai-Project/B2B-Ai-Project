package com._P.eureka.client.delivery.application.service;

import com._P.eureka.client.delivery.application.dto.delivery.DeliveryResponseDto;
import com._P.eureka.client.delivery.application.dto.order.OrderInfoDto;
import com._P.eureka.client.delivery.doamin.common.DeliveryPersonRoleEnum;
import com._P.eureka.client.delivery.doamin.model.Delivery;
import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import com._P.eureka.client.delivery.doamin.model.Order;
import com._P.eureka.client.delivery.doamin.repository.DeliveryPersonRepository;
import com._P.eureka.client.delivery.doamin.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {
  private final DeliveryRepository deliveryRepository;
  private final DeliveryPersonRepository deliveryPersonRepository;


  // 배송 단건 조회
  public DeliveryResponseDto getDelivery(UUID deliveryId) {
    Delivery delivery = findByDeliveryId(deliveryId);
    return DeliveryResponseDto.fromEntity(delivery);
  }

  // 배송에 배송 담당자 지정
  public void updateDeliveryPerson(UUID deliveryId, UUID deliveryPersonId) {
    Delivery delivery = findByDeliveryId(deliveryId);
    DeliveryPerson deliveryPerson = findByDeliveryPersonId(deliveryPersonId);

    // 배송 담당자가 업체 이동 담당자이고, 대기중인 배송 담당자가 아니라면 Error
    if (deliveryPerson.getRole().equals(DeliveryPersonRoleEnum.RECIPIENT_DELIVERY) || !deliveryPerson.is_waiting()) {
      throw new IllegalArgumentException("지정할 수 없는 배송 담당자 입니다.");
    }

    delivery.setDeliveryPersonId(deliveryPerson.getDeliveryPersonId());
    deliveryPerson.set_waiting(false); // 배송 담당자 / 대기중 -> 배송중

    deliveryRepository.save(delivery);
    deliveryPersonRepository.save(deliveryPerson);
  }



  public Delivery findByDeliveryId(UUID deliveryId) {
    Delivery deliver = deliveryRepository.findById(deliveryId).orElseThrow(() ->
            new IllegalArgumentException("배송 아이디가 존재하지 않습니다."));
    if (deliver.isDeleted()){
      throw new IllegalArgumentException("삭제된 배송 입니다.");
    }
    return deliver;
  }

  public DeliveryPerson findByDeliveryPersonId(UUID deliveryPersonId){
    DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(deliveryPersonId).orElseThrow(() ->
            new IllegalArgumentException("배송 담당자가 존재하지 않습니다."));

    if (deliveryPerson.isDeleted()){
      throw new IllegalArgumentException("삭제된 배송 담당자 입니다.");
    }
    return deliveryPerson;
  }

  public Page<DeliveryResponseDto> search(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC; // 오름차순 or 내림차순
    Sort sort = Sort.by(direction, sortBy); // 정렬 방향으로 Sort 객체 생성
    Pageable pageable = PageRequest.of(page, size, sort);

    // is_deleted가 false인 Order 목록만 조회
    Page<Delivery> orderPage = deliveryRepository.findAllByIsDeletedFalse(pageable);

    return orderPage.map(DeliveryResponseDto::fromEntity);
  }
}
