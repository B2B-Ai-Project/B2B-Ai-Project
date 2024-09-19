package com._P.eureka.client.delivery.hub_route.service;

import com._P.eureka.client.delivery.client.ObjectClient;
import com._P.eureka.client.delivery.dto.HubResponseDto;
import com._P.eureka.client.delivery.hub_route.dto.HubRouteCreateDto;
import com._P.eureka.client.delivery.hub_route.dto.HubRouteResponseDto;
import com._P.eureka.client.delivery.hub_route.dto.HubRouteUpdateDto;
import com._P.eureka.client.delivery.hub_route.dto.SubpathCreateDto;
import com._P.eureka.client.delivery.hub_route.entity.Hub_Route;
import com._P.eureka.client.delivery.hub_route.entity.Subpath;
import com._P.eureka.client.delivery.hub_route.repository.HubRouteRepository;
import com._P.eureka.client.delivery.hub_route.repository.SubpathRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteService {
    private final HubRouteRepository hubRouteRepository;
    private final SubpathRepository subpathRepository;
    private final ObjectClient objectClient;

    public HubRouteResponseDto createHubRoute(HubRouteCreateDto requestDto) {
        // TODO
        // 마스터 관리자 / 스케줄러만 접근 허용

        HubResponseDto endHub = objectClient.getHubById(requestDto.getEndHubId());
        HubResponseDto startHub = objectClient.getHubById(requestDto.getStartHubId());

    }

    @Transactional
    public String deleteHubRoute(UUID hubRouteId) {
        // TODO
        // 마스터 관리자 / 스케줄러만 접근 허용

        Hub_Route hubRoute = checkHubRoute(hubRouteId);
        hubRoute.delete();

        return "삭제가 완료되었습니다.";
    }


    public Page<HubRouteResponseDto> searchHubRoutes(UUID startHubId, UUID endHubId, Pageable pageable) {
        if (startHubId == null && endHubId == null) { // 삭제되지 않은 모든 데이터 검색
            return hubRouteRepository.findAllByIsDeletedFalse(pageable)
                    .map(HubRouteResponseDto::new);
        } else if (endHubId == null) { // startHubId만으로 검색
            return hubRouteRepository.findByStartHubIdAndIsDeletedFalse(startHubId, pageable)
                    .map(HubRouteResponseDto::new);
        } else if (startHubId == null) { // endHubId만으로 검색
            return hubRouteRepository.findByEndHubIdAndIsDeletedFalse(endHubId, pageable)
                    .map(HubRouteResponseDto::new);
        } else { // startHubId, endHubId로 검색
            return hubRouteRepository.findByStartHubIdAndEndHubIdAndIsDeletedFalse(startHubId, endHubId, pageable)
                    .map(HubRouteResponseDto::new);
        }
    }


    @Transactional
    public HubRouteResponseDto updateHubRoute(UUID hubRouteId, HubRouteUpdateDto requestDto) {
        // TODO
        // 마스터 관리자 / 스케줄러만 접근 허용

        Hub_Route hubRoute = checkHubRoute(hubRouteId);

        // startHubId와 endHubId가 동일한 경우 체크
        if (requestDto.getStartHubId().equals(requestDto.getEndHubId())) {
            throw new IllegalArgumentException("출발지와 도착지는 동일할 수 없습니다.");
        }

        // 허브 경로 중복 체크
        Optional<Hub_Route> checkRoute = hubRouteRepository.findByStartHubIdAndEndHubId(requestDto.getStartHubId(), requestDto.getEndHubId());

        // 현재 업데이트할 허브 경로가 기존에 있는 경로와 다르다면 중복 체크
        if (checkRoute.isPresent() && !checkRoute.get().getRouteId().equals(hubRoute.getRouteId())) {
            throw new IllegalArgumentException("이미 존재하는 허브 경로입니다.");
        }

        // 허브 경로 업데이트
        hubRoute.update(requestDto);

        // DTO로 만들어서 리턴
        return new HubRouteResponseDto(hubRoute);
    }



    public String createSubpath(SubpathCreateDto subpathCreateDto) {
        // TODO
        // 마스터 관리자 / 스케줄러만 접근 허용

        // hubroute id가 같고, startHub와 endHub가 같은 데이터 추가 불가
        Hub_Route hubRoute = hubRouteRepository.findByRouteIdAndIsDeletedFalse(subpathCreateDto.getHubRouteId())
                .orElseThrow(
                        () -> new NullPointerException("해당 허브 경로는 존재하지 않습니다.")
                );

        boolean isSubpathExists = subpathRepository.existsByHubRouteAndStartHubIdAndEndHubIdAndIsDeletedFalse(
                hubRoute, subpathCreateDto.getStartHubId(), subpathCreateDto.getEndHubId()
        );

        if (isSubpathExists) {
            throw new IllegalArgumentException("해당 경로에는 이미 동일한 서브패스가 존재합니다.");
        }

        subpathRepository.save(subpathCreateDto.toEntity(hubRoute));

        return "생성 완료";
    }


    // 허브경로 id가 중복이 아니면 허브 리턴, is_deleted = true면 조회, 수정, 삭제 불가
    public Hub_Route checkHubRoute(UUID hubRouteId){
        Hub_Route hubRoute = hubRouteRepository.findById(hubRouteId).orElseThrow(
                () -> new NullPointerException("Id에 해당하는 허브 경로가 없습니다.")
        );

        if(hubRoute.isDeleted()){
            throw new IllegalArgumentException("이미 삭제 요청된 허브 경로입니다.");
        }
        return hubRoute;
    }

    // 위도와 경도를 기준으로 두 지점 간의 거리 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구의 반지름 (킬로미터 단위)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
