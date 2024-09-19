package com._P.eureka.client.delivery.hub_route.entity;

import com._P.eureka.client.delivery.hub_route.dto.HubRouteUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com._P.eureka.client.delivery.hub_route.entity.Subpath;


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
    private UUID routeId;

    private UUID startHubId;

    private UUID endHubId;

    @OneToMany(mappedBy = "hubRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subpath> subpaths = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void delete(){
        this.isDeleted = true;

        // 하위 엔티티까지 연쇄 삭제
        for(Subpath subpath : subpaths){
            subpath.delete();
        }
    }

    public void update(HubRouteUpdateDto requestDto){
        this.startHubId = requestDto.getStartHubId();
        this.endHubId = requestDto.getEndHubId();
    }
}
