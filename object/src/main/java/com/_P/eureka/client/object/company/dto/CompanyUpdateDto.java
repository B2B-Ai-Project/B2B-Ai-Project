package com._P.eureka.client.object.company.dto;

import com._P.eureka.client.object.common.CompanyTypeEnum;
import lombok.Getter;

@Getter
public class CompanyUpdateDto {
    private String name;
    private CompanyTypeEnum companyType;
    private String hubId;
    private String address;
}
