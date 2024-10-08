package com._P.eureka.client.auth.domain.model;


import lombok.Getter;

@Getter
public enum UserRoleEnum {
  //HUB_DELIVERY;

    COMPANY(Authority.COMPANY), // 허브 소속 업체 권한
    DELIVERY_PERSON(Authority.DELIVERY_PERSON), // 배달담당자 권한
    MASTER(Authority.MASTER), // 마스터 권한
    HUB_MANAGER(Authority.HUB_MANAGER), // 허브매니저 권한
    HUB_DELIVERY(Authority.HUB_DELIVERY);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String COMPANY = "ROLE_COMPANY";
        public static final String DELIVERY_PERSON = "ROLE_DELIVERY_PERSON";
        public static final String MASTER = "ROLE_MASTER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String HUB_DELIVERY = "ROLE_HUB_DELIVERY";
    }


}
