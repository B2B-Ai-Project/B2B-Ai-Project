package com._P.eureka.client.object.company.service;

import com._P.eureka.client.object.company.constant.CompanySearchType;
import com._P.eureka.client.object.company.dto.CompanyCreateDto;
import com._P.eureka.client.object.company.dto.CompanyResponseDto;
import com._P.eureka.client.object.company.dto.CompanyUpdateDto;
import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.company.repository.CompanyRepository;
import com._P.eureka.client.object.hub.entity.Hub;
import com._P.eureka.client.object.hub.repository.HubRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HubRepository hubRepository;
    public String create(CompanyCreateDto requestDto) {
        // TODO
        // === MASTER, HUB_MANAGER만 접근 허용 ===

        // 허브 존재 체크
        Hub hub = getHub(requestDto.getHubId());

        Company company = requestDto.toEntity(hub);
        isCompanyAlreadyExists(company); // 중복 체크

        companyRepository.save(company);

        return "생성 완료";
    }

    @Transactional
    public CompanyResponseDto update(CompanyUpdateDto requestDto, String companyId) {
        // TODO
        // === MASTER, HUB_MANAGER, COMPANY만 접근 허용 ===

        // 허브 존재 체크
        Hub hub = getHub(requestDto.getHubId());

        Company company = checkCompany(companyId); // 이미 삭제된 회사인지 체크
        isCompanyAlreadyExists(company); // 중복 체크

        company.update(requestDto,hub);

        return new CompanyResponseDto(company);
    }

    @Transactional
    public String delete(String companyId) {
        // TODO
        // === MASTER, HUB_MANAGER만 접근 가능 ===

        Company company = checkCompany(companyId); // 이미 삭제된 회사인지 체크

        company.delete();
        return "삭제 완료";
    }

    public CompanyResponseDto getOne(String companyId) {
        Company company = checkCompany(companyId); // 이미 삭제된 회사인지 체크

        return new CompanyResponseDto(company);
    }

    public Page<CompanyResponseDto> search(CompanySearchType searchType, String searchValue, Pageable pageable) {
        // 입력 값 없으면 모든 값 리턴
        if (searchValue == null || searchValue.isBlank()){
            return companyRepository.findAll(pageable).map(CompanyResponseDto::new);
        }

        return switch (searchType){
            case NAME ->
                    companyRepository.findByNameContainingIgnoreCase(searchValue,pageable)
                            .map(CompanyResponseDto::new);
            case TYPE ->
                    companyRepository.findByCompanyTypeContainingIgnoreCase(searchValue,pageable)
                            .map(CompanyResponseDto::new);
        };
    }


    // 관리 허브 ID가 존재하는 허브인지 확인
    private Hub getHub(String hubId){
        return hubRepository.findById(hubId).orElseThrow(
                () -> new NullPointerException("해당 허브는 존재하지 않습니다.")
        );
    }

    // company 값 읽어오기 / is_deleted = true면 조회, 수정, 삭제 불가
    private Company checkCompany(String companyId){
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new NullPointerException("해당 업체는 존재하지 않습니다.")
        );

        if(company.isDeleted()){
            throw new IllegalArgumentException("이미 삭제 요청된 업체입니다.");
        }
        return company;
    }

    // address, name 모두 같으면 같은 회사 / 추가, 수정 불가
    private void isCompanyAlreadyExists(Company company){
        Optional<Company> checkCompany = companyRepository.findByNameAndAddress(company.getName(),company.getAddress());
        if(checkCompany.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 업체입니다.");
        }
    }
}
