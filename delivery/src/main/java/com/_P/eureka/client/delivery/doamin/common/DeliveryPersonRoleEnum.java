package com._P.eureka.client.delivery.doamin.common;

import lombok.Getter;

@Getter
public enum DeliveryPersonRoleEnum {
  HUB_DELIVERY, // 공통 허브 이동 담당자
  RECIPIENT_DELIVERY; // 업체 이동 담당자
}
