package com._P.eureka.client.object.hub.entity;

import com._P.eureka.client.object.hub.dto.HubUpdateDto;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "p_hub")
public class Hub {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String hub_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private int latitude;

    private int longitude;

    @Column(nullable = false)
    private boolean is_deleted = false;

    public void delete() {
        this.is_deleted = true;
    }

    public void update(HubUpdateDto requestDto) {
        this.name = requestDto.getName();
        this.address = requestDto.getAddress();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }
}
