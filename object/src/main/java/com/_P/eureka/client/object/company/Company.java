package com._P.eureka.client.object.company;

import com._P.eureka.client.object.common.CompanyTypeEnum;
import com._P.eureka.client.object.hub.entity.Hub;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String company_id;

    @Column(nullable = false)
    private CompanyTypeEnum companyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    private String address;

    @Column(nullable = false)
    private boolean is_deleted = false;
}
