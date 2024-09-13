package com._P.eureka.client.object.product.service;

import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.company.repository.CompanyRepository;
import com._P.eureka.client.object.company.service.CompanyService;
import com._P.eureka.client.object.hub.entity.Hub;
import com._P.eureka.client.object.hub.repository.HubRepository;
import com._P.eureka.client.object.hub.service.HubService;
import com._P.eureka.client.object.product.dto.ProductCreateDto;
import com._P.eureka.client.object.product.dto.ProductResponseDto;
import com._P.eureka.client.object.product.dto.ProductUpdateDto;
import com._P.eureka.client.object.product.entity.Product;
import com._P.eureka.client.object.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyService companyService;
    private final HubService hubService;

    public String create(ProductCreateDto requestDto) {
        // TODO
        // 권한 체크

        // 업체, 허브 존재 여부 확인
        Company company = companyService.checkCompany(requestDto.getCompanyId());
        Hub hub = hubService.checkHub(requestDto.getHubId());

        Product product = requestDto.toEntity(company,hub);

        // 제품 중복 체크
        isProductAlreadyExists(product);
        productRepository.save(product);

        return "생성 완료";
    }

    @Transactional
    public ProductResponseDto update(ProductUpdateDto requestDto, UUID productId) {
        // TODO
        // 권한 체크

        // 업체, 허브 존재 여부 확인
        Company company = companyService.checkCompany(requestDto.getCompanyId());
        Hub hub = hubService.checkHub(requestDto.getHubId());

        // 이미 삭제된 제품인지 체크
        Product product = checkProduct(productId);
        // 제품 중복 체크
        Optional<Product> checkProduct = productRepository.findByCompanyAndProductName(product.getCompany(),requestDto.getProductName());

        if(checkProduct.isPresent() && !checkProduct.get().equals(product)){
            throw new IllegalArgumentException("이미 존재하는 제품입니다.");
        }

        product.update(requestDto,company,hub);

        return new ProductResponseDto(product);
    }

    @Transactional
    public String delete(UUID productId) {
        // 이미 삭제된 제품인지 체크
        Product product = checkProduct(productId);

        product.delete();
        return "삭제 완료";
    }

    public ProductResponseDto getOne(UUID productId) {
        // 이미 삭제된 제품인지 체크
        Product product = checkProduct(productId);

        return new ProductResponseDto(product);
    }

    public Page<ProductResponseDto> search(String searchValue, Pageable pageable) {
        // 입력 값 없으면 모든 값 리턴
        if (searchValue == null || searchValue.isBlank()){
            return productRepository.findAllByIsDeletedFalse(pageable).map(ProductResponseDto::new);
        }

        return productRepository.findByProductNameAndIsDeletedFalse(searchValue,pageable).map(ProductResponseDto::new);

    }

    // 업체, 제품명이 동일하면 같은 제품 / 추가, 수정 불가
    private void isProductAlreadyExists(Product product){
        Optional<Product> checkProduct = productRepository.findByCompanyAndProductName(product.getCompany(),product.getProductName());
        if(checkProduct.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 제품입니다.");
        }
    }

    // 이미 삭제 요청 된 제품인지 확인 / 조회, 수정 불가
    private Product checkProduct(UUID productId){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NullPointerException("ID에 해당하는 허브가 없습니다.")
        );

        if(product.isDeleted()){
            throw new IllegalArgumentException("이미 삭제 요청된 허브입니다.");
        }
        return product;
    }
}