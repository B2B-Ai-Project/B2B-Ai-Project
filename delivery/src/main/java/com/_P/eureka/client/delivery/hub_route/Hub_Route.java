package com._P.eureka.client.delivery.hub_route;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_hub_route")
public class Hub_Route {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String route_id;

    private String start_hub;

    private String end_hub;

    // 소요 시간 (단위 : 분)
    private int route_time;

    @Column(nullable = false)
    private boolean is_deleted = false;
}
