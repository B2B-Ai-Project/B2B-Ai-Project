package com._P.eureka.client.object.company.dto;

import com._P.eureka.client.object.company.entity.Company;
import lombok.Getter;

@Getter
public class CompanyResponseDto {
    private String company_id;
    private String name;
    private String companyType;
    private String hubId;
    private String address;

    public CompanyResponseDto(Company company){
        this.company_id = company.getCompany_id();
        this.name = company.getName();
        this.companyType = company.getCompanyType().toString();
        this.hubId = company.getHub().getHubId();
        this.address = company.getAddress();
    }
}