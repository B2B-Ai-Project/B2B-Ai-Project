package com._P.eureka.client.object.company.dto;

import com._P.eureka.client.object.common.CompanyTypeEnum;
import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.hub.entity.Hub;
import lombok.Getter;

@Getter
public class CompanyCreateDto {
    private String name;
    private CompanyTypeEnum companyType;
    private String hubId;
    private String address;

    public Company toEntity(Hub hub) {
        return Company.builder()
                .name(this.name)
                .companyType(this.companyType)
                .hub(hub)
                .address(this.address)
                .build();
    }
}
