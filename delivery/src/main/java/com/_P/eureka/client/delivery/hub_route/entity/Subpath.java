package com._P.eureka.client.delivery.hub_route.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_sub_path")
public class Subpath {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID subPathId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "hub_route_id")
    private Hub_Route hubRoute;

    private UUID startHubId;

    private UUID endHubId;

    private int sequence;

//    private long distance;

    // 소요 시간 (단위 : 분)
    private int routeTime;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void delete() {
        this.isDeleted = true;
    }
}
