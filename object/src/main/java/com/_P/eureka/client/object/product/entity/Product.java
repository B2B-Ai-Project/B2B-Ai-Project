package com._P.eureka.client.object.product.entity;


import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.hub.entity.Hub;
import com._P.eureka.client.object.product.dto.ProductUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_product",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"company_id", "productName"})
        })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @Column(nullable = false)
    private String productName;

    private String productContent;

    @Column(nullable = false)
    private boolean isDeleted;

    public void update(ProductUpdateDto requestDto, Company company, Hub hub) {
        this.company = company;
        this.hub = hub;
        this.productName = requestDto.getProductName();
        this.productContent = requestDto.getProductContent();
    }

    public void delete() {
        this.isDeleted = true;
    }
}