package com._P.eureka.client.delivery.hub_route.controller;


import com._P.eureka.client.delivery.hub_route.dto.HubRouteCreateDto;
import com._P.eureka.client.delivery.hub_route.dto.HubRouteResponseDto;
import com._P.eureka.client.delivery.hub_route.dto.HubRouteUpdateDto;
import com._P.eureka.client.delivery.hub_route.dto.SubpathCreateDto;
import com._P.eureka.client.delivery.hub_route.service.HubRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub-route")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    // 출발지와 도착지를 입력 받아 HubRoute를 생성
    @PostMapping
    public HubRouteResponseDto createHubRoute(@RequestBody HubRouteCreateDto requestDto) {
        return hubRouteService.createHubRoute(requestDto);
    }

    // 허브 경로 삭제 (하위 경로(subpath)까지 같이 삭제)
    @DeleteMapping("{hubRouteId}")
    public String deleteHubRoute(@PathVariable UUID hubRouteId) {
        return hubRouteService.deleteHubRoute(hubRouteId);
    }

    // 출발지, 도착지, 중간 경유지에 따라 경로 검색
    @GetMapping("/search")
    public Page<HubRouteResponseDto> searchHubRoutes(
            @RequestParam(required = false) UUID startHubId,
            @RequestParam(required = false) UUID endHubId,
            @PageableDefault(size = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return hubRouteService.searchHubRoutes(startHubId, endHubId, pageable);
    }

    // 하위 경로를 따로 추가하는 API
    @PostMapping("/subpath")
    public String createSubpath(@RequestBody SubpathCreateDto subpathCreateDto) {
        return hubRouteService.createSubpath(subpathCreateDto);
    }

    // 허브 경로 수정 API (출발지/도착지를 변경)
    @PutMapping("{hubRouteId}")
    public HubRouteResponseDto updateHubRoute(
            @PathVariable UUID hubRouteId,
            @RequestBody HubRouteUpdateDto requestDto) {
        return hubRouteService.updateHubRoute(hubRouteId, requestDto);
    }
}