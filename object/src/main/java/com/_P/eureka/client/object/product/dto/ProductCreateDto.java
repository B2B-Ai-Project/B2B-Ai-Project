package com._P.eureka.client.object.product.dto;

import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.hub.entity.Hub;
import com._P.eureka.client.object.product.entity.Product;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductCreateDto {
    private UUID companyId;
    private UUID hubId;
    private String productName;
    private String productContent;

    public Product toEntity(Company company, Hub hub) {
        return Product.builder()
                .company(company)
                .hub(hub)
                .productName(this.productName)
                .productContent(this.productContent)
                .build();
    }
}
