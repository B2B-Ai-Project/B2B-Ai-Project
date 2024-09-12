package com._P.eureka.client.object.company.controller;

import com._P.eureka.client.object.company.constant.CompanySearchType;
import com._P.eureka.client.object.company.dto.CompanyCreateDto;
import com._P.eureka.client.object.company.dto.CompanyResponseDto;
import com._P.eureka.client.object.company.dto.CompanyUpdateDto;
import com._P.eureka.client.object.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {
    private final CompanyService companyService;

    // TODO
    // User Token 확인, 검사

    // === MASTER, HUB_MANAGER ===
    @PostMapping
    public String create(@RequestBody CompanyCreateDto requestDto){
        return companyService.create(requestDto);
    }

    // === MASTER, HUB_MANAGER, COMPANY ===
    @PutMapping("{companyId}")
    public CompanyResponseDto update(@RequestBody CompanyUpdateDto requestDto, @PathVariable String companyId){
        return companyService.update(requestDto,companyId);
    }

    // === MASTER, HUB_MANAGER ===
    @DeleteMapping("{companyId}")
    public String delete(@PathVariable String companyId){
        return companyService.delete(companyId);
    }

    // === 모든 사용자 접근 가능 ===
    @GetMapping("{companyId}")
    public CompanyResponseDto getOne(@PathVariable String companyId){
        return companyService.getOne(companyId);
    }

    // 회사 종류 / 회사 이름 중 선택해서 검색
    @GetMapping
    public Page<CompanyResponseDto> search(
            @RequestParam(required = false, name = "searchType") CompanySearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return companyService.search(searchType, searchValue, pageable);
    }
}
