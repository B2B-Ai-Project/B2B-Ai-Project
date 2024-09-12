package com._P.eureka.client.delivery.doamin.common;

public enum DeliveryRoleEnum {
  HUB_WAITING, // 허브 이동 대기중
  HUB_DELIVERY, // 허브 이동중
  HUB_ARRIVED, // 목적지 허브 도착
  RECIPIENT_DELIVERY // 수령 업체로 배송중;
}
