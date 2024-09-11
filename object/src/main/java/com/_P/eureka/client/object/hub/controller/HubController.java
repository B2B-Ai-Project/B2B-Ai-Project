package com._P.eureka.client.object.hub.controller;

import com._P.eureka.client.object.hub.dto.HubCreateDto;
import com._P.eureka.client.object.hub.dto.HubResponseDto;
import com._P.eureka.client.object.hub.dto.HubUpdateDto;
import com._P.eureka.client.object.hub.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hub")
public class HubController {

    // TODO
    // User 검증

    private final HubService hubService;

    // ## 모든 로그인 사용자가 가능
    @GetMapping("{hub_id}")
    public HubResponseDto getOne(@PathVariable (name = "hub_id") String hubId){
        return hubService.getOne(hubId);

    }

    // 허브 이름으로 검색
    @GetMapping
    public Page<HubResponseDto> search(
           @RequestParam(required = false, name = "searchValue") String searchValue,
           @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return hubService.search(searchValue,pageable);
    }

    // ## 마스터 관리자만 가능
    @PostMapping
    public String create(HubCreateDto requestDto){

        return hubService.create(requestDto);
    }

    @PutMapping("{hub_id}")
    public HubResponseDto update(HubUpdateDto requestDto, @PathVariable (name = "hub_id") String hubId){

        return hubService.update(requestDto,hubId);
    }

    @DeleteMapping("{hub_id}")
    public String delete(@PathVariable (name = "hub_id") String hubId){

        return hubService.delete(hubId);
    }
}
