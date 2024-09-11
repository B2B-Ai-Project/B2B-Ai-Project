package com._P.eureka.client.delivery.application.service;

import com._P.eureka.client.delivery.application.dto.DeliveryPersonResDto;
import com._P.eureka.client.delivery.client.AuthClient;
import com._P.eureka.client.delivery.client.HubClient;
import com._P.eureka.client.delivery.doamin.model.DeliveryPerson;
import com._P.eureka.client.delivery.doamin.common.DeliveryPersonRoleEnum;
import com._P.eureka.client.delivery.doamin.repository.DeliveryPersonRepository;
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
public class DeliveryPersonService {
  private final DeliveryPersonRepository deliveryPersonRepository;
  private final AuthClient authClient;
  private final HubClient hubClient;

  // 배송 담당자 생성
  public DeliveryPersonResDto create(UUID userId) {
    // FeignClient 를 통해 Auth Application 으로 이동
    String email = authClient.create(userId); // slackId

    // DeliveryPerson 객체를 빌더 패턴으로 생성하고 저장
    DeliveryPerson deliveryPerson = deliveryPersonRepository.save(
            DeliveryPerson.builder()
                    .deliveryPersonId(userId)
                    .hubId(null) // 소속 허브가 없는 경우 null 설정
                    .email(email) // authClient로 받은 email 설정
                    .role(DeliveryPersonRoleEnum.HUB_DELIVERY) // 기본 역할을 HUB_DELIVERY로 설정
                    .is_waiting(true) // 대기 상태로 설정
                    .build()
    );

    return DeliveryPersonResDto.fromEntity(deliveryPerson);
  }


//   배송 담당자 내 정보 조회
//  public DeliveryPersonResDto getUser(CustomUserDetails userDetails) {
//    UUID deliveryPersonId = userDetails.getDeliveryPersonId();
//    DeliveryPerson user = deliveryPersonRepository.findById(deliveryPersonId).orElseThrow(() ->
//            new IllegalArgumentException("존재하지 않는 사용자 입니다."));
//
//    return DeliveryPersonResDto.fromEntity(user);
//  }
//
//  // 배송 당담자 허브 ID 부여 및 권한 변경 -> 배송 담당자에게 HubId가 부여되면 공통허브 담당자에서 업체 배송 담당자로 권한 변경
//  public DeliveryPersonResDto update(UUID hubId, CustomUserDetails userDetails) {
//    // 배송 담당자 존재 여부 확인
//    UUID deliveryPersonId = userDetails.getDeliveryPersonId();
//    DeliveryPerson user = deliveryPersonRepository.findById(deliveryPersonId).orElseThrow(() ->
//            new IllegalArgumentException("존재하지 않는 사용자 입니다."));
//
//    // 현재 배송중인 배송 담당자인지 검증
//    if (!user.is_waiting()){ // 배송중인 당담자는 권한 및 허브ID 수정 불가
//      throw new IllegalArgumentException("수정이 불가능한 사용자 입니다.");
//    }
//    String getHubId = hubClient.update(hubId); // HubId 검증
//
//    // 권한 변경 및 허브ID 부여, 저장
//    user.updateHubAndRole(getHubId, DeliveryPersonRoleEnum.RECIPIENT_DELIVERY);
//    return DeliveryPersonResDto.fromEntity(deliveryPersonRepository.save(user));
//  }

  public Page<DeliveryPersonResDto> search(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC; // 오름차순 or 내림차순
    Sort sort = Sort.by(direction, sortBy); // 정렬 방향으로 Sort 객체 생성
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<DeliveryPerson> deliveryPersonPage = deliveryPersonRepository.findAll(pageable);

    return deliveryPersonPage.map(DeliveryPersonResDto::new);
  }
}
