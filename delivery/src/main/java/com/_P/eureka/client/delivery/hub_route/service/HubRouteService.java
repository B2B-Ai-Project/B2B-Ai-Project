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

import java.util.*;

@Service
@RequiredArgsConstructor
public class HubRouteService {
    private final HubRouteRepository hubRouteRepository;
    private final SubpathRepository subpathRepository;
    private final ObjectClient objectClient;

    public HubRouteResponseDto createHubRoute(HubRouteCreateDto requestDto) {
        // 마스터 관리자 / 스케줄러만 접근 허용 (TODO)
        HubResponseDto startHub = objectClient.getHubById(requestDto.getStartHubId());
        HubResponseDto endHub = objectClient.getHubById(requestDto.getEndHubId());

        // 서브패스를 포함한 최단 경로 연산
        List<Subpath> subpaths = calculateShortestPath(requestDto.getStartHubId(), requestDto.getEndHubId());

        // 서브패스를 포함한 허브 경로 저장
        Hub_Route hubRoute = new Hub_Route(requestDto, subpaths);
        hubRouteRepository.save(hubRoute);

        // 서브패스 저장
        subpaths.forEach(subpath -> {
            subpath.setHubRoute(hubRoute);
            subpathRepository.save(subpath);
        });

        return new HubRouteResponseDto(hubRoute);
    }

    @Transactional
    public String deleteHubRoute(UUID hubRouteId) {
        // 마스터 관리자 / 스케줄러만 접근 허용 (TODO)
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
        // 마스터 관리자 / 스케줄러만 접근 허용 (TODO)
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
        Hub_Route hubRoute = hubRouteRepository.findByRouteIdAndIsDeletedFalse(subpathCreateDto.getHubRouteId())
                .orElseThrow(() -> new NullPointerException("해당 허브 경로는 존재하지 않습니다."));

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

    // Dijkstra 알고리즘을 사용한 최단 경로 계산 메소드
    private List<Subpath> calculateShortestPath(UUID startHubId, UUID endHubId) {
        Map<UUID, Map<UUID, Double>> graph = createGraph(); // 허브 간의 거리 맵 생성
        return dijkstra(startHubId, endHubId, graph);
    }

    // 허브 간의 거리 정보를 포함한 그래프 생성
    private Map<UUID, Map<UUID, Double>> createGraph() {
        // 허브 ID를 키로 하고, 각 허브와 연결된 허브의 ID와 거리 정보 저장
        Map<UUID, Map<UUID, Double>> graph = new HashMap<>();
        // 실제 데이터로 채우거나, 허브 간의 경로 데이터를 가져와서 거리 정보 추가
        return graph;
    }

    private List<Subpath> dijkstra(UUID startHubId, UUID endHubId, Map<UUID, Map<UUID, Double>> graph) {
        // 각 허브까지의 최단 거리를 저장하는 맵
        Map<UUID, Double> distances = new HashMap<>();
        // 최단 경로 추적을 위해 이전 허브를 저장하는 맵
        Map<UUID, UUID> previous = new HashMap<>();
        // 우선순위 큐를 사용하여 최소 거리 허브를 빠르게 찾음
        PriorityQueue<UUID> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        // 초기화: 시작 허브는 거리 0, 나머지는 무한대로 설정
        for (UUID hubId : graph.keySet()) {
            if (hubId.equals(startHubId)) {
                distances.put(hubId, 0.0);
            } else {
                distances.put(hubId, Double.MAX_VALUE);
            }
            queue.add(hubId);
        }

        // 큐가 빌 때까지 반복
        while (!queue.isEmpty()) {
            // 현재 최단 거리 허브를 가져옴
            UUID current = queue.poll();
            // 도착 허브에 도달하면 종료
            if (current.equals(endHubId)) break;

            // 현재 허브와 연결된 모든 이웃 허브들을 탐색
            Map<UUID, Double> neighbors = graph.get(current);
            for (UUID neighbor : neighbors.keySet()) {
                // 현재 허브를 거쳐 이웃 허브에 도달하는 거리 계산
                double alt = distances.get(current) + neighbors.get(neighbor);
                // 만약 계산된 거리가 기존의 최단 거리보다 짧다면 업데이트
                if (alt < distances.get(neighbor)) {
                    distances.put(neighbor, alt);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // 최단 경로 추적: 도착 허브부터 시작하여 역으로 이전 허브를 찾아 경로를 생성
        List<UUID> path = new ArrayList<>();
        for (UUID at = endHubId; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // 최단 경로를 Subpath 객체의 리스트로 변환
        List<Subpath> subpaths = new ArrayList<>();
        int sequence = 1; // 경로의 순서를 나타내는 변수
        for (int i = 0; i < path.size() - 1; i++) {
            UUID start = path.get(i);
            UUID end = path.get(i + 1);
            Subpath subpath = new Subpath(start, end, sequence++);
            subpaths.add(subpath);
        }

        return subpaths;
    }

}