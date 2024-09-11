package com._P.eureka.client.auth.application.service;

import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import com._P.eureka.client.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  // 배송 담당자 생성 검증
  public String checkUser(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(()->
            new IllegalArgumentException("존재하지 않는 유저 입니다."));

    if (!user.getRole().equals(UserRoleEnum.HUB_DELIVERY)){
        throw new IllegalArgumentException("잘못된 권한 접근 입니다.");
    }
    // DeliveryPerson Application 에서 email(SlackId)가 필요하다
    return user.getEmail();
  }

}
