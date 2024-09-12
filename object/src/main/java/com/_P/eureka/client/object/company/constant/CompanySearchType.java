package com._P.eureka.client.object.company.constant;

import lombok.Getter;

@Getter
public enum CompanySearchType {
    TYPE("회사유형"),
    NAME("회사명");

    private final String description;

    CompanySearchType(String description){
        this.description = description;
    }
}
