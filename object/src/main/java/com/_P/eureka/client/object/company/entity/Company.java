package com._P.eureka.client.object.company.entity;

import com._P.eureka.client.object.common.CompanyTypeEnum;
import com._P.eureka.client.object.company.dto.CompanyUpdateDto;
import com._P.eureka.client.object.hub.entity.Hub;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_company",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "address"})
})
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String company_id;

    private String name;

    @Column(nullable = false)
    private CompanyTypeEnum companyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    private String address;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void update(CompanyUpdateDto requestDto, Hub hub) {
        this.name = requestDto.getName();
        this.companyType = requestDto.getCompanyType();
        this.hub = hub;
        this.address = requestDto.getAddress();
    }

    public void delete() {
        this.isDeleted = true;
    }
}