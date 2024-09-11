package com._P.eureka.client.object.hub.service;

import com._P.eureka.client.object.hub.entity.Hub;
import com._P.eureka.client.object.hub.repository.HubRepository;
import com._P.eureka.client.object.hub.dto.HubCreateDto;
import com._P.eureka.client.object.hub.dto.HubResponseDto;
import com._P.eureka.client.object.hub.dto.HubUpdateDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;

    public HubResponseDto getOne(String hubId) {
        Hub hub = findHub(hubId);

        // Dto로 만들어서 리턴
        return new HubResponseDto(hub);
    }

    public Page<HubResponseDto> search(String searchValue, Pageable pageable) {
        if (searchValue == null || searchValue.isBlank()) {
            return hubRepository.findAllByIsDeletedFalse(pageable)
                    .map(HubResponseDto::new);  // DTO로 변환
        }
        return hubRepository.findByNameContaining(searchValue, pageable)
                .map(HubResponseDto::new);  // DTO로 변환
    }

    public String create(HubCreateDto requestDto) {
        // master 권한 체크
        isUserMaster();

        // name, address 중복 체크
        Optional checkNameHub = hubRepository.findByName(requestDto.getName());
        Optional checkAddressHub = hubRepository.findByAddress(requestDto.getAddress());

        if(checkNameHub.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 허브 이름입니다.");
        }

        if(checkAddressHub.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 주소입니다.");
        }
        return "생성 완료";
    }

    @Transactional
    public HubResponseDto update(HubUpdateDto requestDto, String hubId) {
        // master 권한 체크
        isUserMaster();

        Hub hub = findHub(hubId);

        // name, address 중복 체크
        Optional checkNameHub = hubRepository.findByName(requestDto.getName());
        Optional checkAddressHub = hubRepository.findByAddress(requestDto.getAddress());

        if(checkNameHub.isPresent() && !checkNameHub.get().equals(hub)){
            throw new IllegalArgumentException("이미 존재하는 허브 이름입니다.");
        }

        if(checkAddressHub.isPresent() && !checkNameHub.get().equals(hub)){
            throw new IllegalArgumentException("이미 존재하는 주소입니다.");
        }

        hub.update(requestDto);

        // Dto로 만들어서 리턴
        return new HubResponseDto(hub);
    }

    @Transactional
    public String delete(String hubId) {
        // master 권한 체크
        isUserMaster();

        Hub hub = findHub(hubId);

        if(hub.is_deleted()){
            throw new IllegalArgumentException("이미 삭제 요청된 허브입니다.");
        }

        hub.delete();

        return "삭제가 완료되었습니다.";
    }

    // 허브 id가 중복이 아니면 허브 리턴
    private Hub findHub(String hubId){
        Hub hub = hubRepository.findById(hubId).orElseThrow(
                () -> new NullPointerException("Id에 해당하는 허브가 없습니다.")
        );
        return hub;
    }

    // TODO
    // 사용자 역할 체크
    // 생성, 수정, 삭제는 마스터 관리자만 가능
    private void isUserMaster(){
        // 토큰에서 값 읽어온 뒤, master가 아니면 exception 던지는 방식으로 구현
    }
}