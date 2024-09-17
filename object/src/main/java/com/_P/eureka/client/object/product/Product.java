package com._P.eureka.client.object.product;

import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.hub.entity.Hub;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_product")
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
    private String product_name;

    private String product_content;

    private Integer quantity; // 보유 수량

    @Column(nullable = false)
    private boolean is_deleted;

    // 수량 확인 및 증감
    public void decreaseQuantity(Integer orderQuantity){
        if (this.quantity >= orderQuantity){
            this.quantity = this.quantity - orderQuantity;
        }else {
            throw new RuntimeException("수량이 부족합니다");
        }
    }
}
