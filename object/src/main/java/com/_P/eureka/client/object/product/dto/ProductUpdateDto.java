package com._P.eureka.client.object.product.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductUpdateDto {
    private UUID companyId;
    private UUID hubId;
    private String productName;
    private String productContent;
}
