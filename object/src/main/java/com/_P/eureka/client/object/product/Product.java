package com._P.eureka.client.object.product;

import com._P.eureka.client.object.company.Company;
import com._P.eureka.client.object.hub.Hub;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String product_id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @Column(nullable = false)
    private String product_name;

    private String product_content;

    @Column(nullable = false)
    private boolean is_deleted;

}
