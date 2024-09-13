package com._P.eureka.client.object.company.dto;

import com._P.eureka.client.object.common.CompanyTypeEnum;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CompanyUpdateDto {
    private String name;
    private CompanyTypeEnum companyType;
    private UUID hubId;
    private String address;
}
