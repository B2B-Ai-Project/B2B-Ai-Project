package com._P.eureka.client.object.company.dto;

import com._P.eureka.client.object.company.entity.Company;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CompanyResponseDto {
    private UUID companyId;
    private String name;
    private String companyType;
    private UUID hubId;
    private String address;

    public CompanyResponseDto(Company company){
        this.companyId = company.getCompanyId();
        this.name = company.getName();
        this.companyType = company.getCompanyType().toString();
        this.hubId = company.getHub().getHubId();
        this.address = company.getAddress();
    }
}