package com._P.eureka.client.object.product.dto;

import com._P.eureka.client.object.product.entity.Product;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductResponseDto {
    private UUID productId;
    private UUID companyId;
    private UUID hubId;
    private String productName;
    private String productContent;
    private boolean isDeleted;

    public ProductResponseDto(Product product){
        this.productId = product.getProductId();
        this.companyId = product.getCompany().getCompanyId();
        this.hubId = product.getHub().getHubId();
        this.productName = product.getProductName();
        this.productContent = product.getProductContent();
        this.isDeleted = product.isDeleted();
    }
}