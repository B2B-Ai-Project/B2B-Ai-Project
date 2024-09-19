package com._P.eureka.client.object.orderValidate.service;

import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.company.repository.CompanyRepository;
import com._P.eureka.client.object.orderValidate.dto.RequestOrderDto;
import com._P.eureka.client.object.orderValidate.dto.ResponseOrderDto;

import com._P.eureka.client.object.product.entity.Product;
import com._P.eureka.client.object.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderValidateService {
  private final CompanyRepository companyRepository;
  private final ProductRepository productRepository;

  // 공급업체 및 수령업체 존재 검증, 제품ID 존재 검증 및 수량 확인
  public ResponseOrderDto orderValidate(RequestOrderDto request) {
    // 공급 업체 검증
    Company supplyCompany = checkCompany(request.getSupplierCompanyId());
    // 수령 업체 검증
    Company recipientCompany = checkCompany(request.getRecipientCompanyId());

    // 제품 검증
    Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 제품 입니다."));
    // 실제로 제품에 등록된 공급업체와 Dto를 통해 전달받은 공급업체가 같은지 비교
    if (!product.getCompany().getCompanyId().equals(request.getSupplierCompanyId())) {
      throw new IllegalArgumentException("공급업체가 일치하지 않습니다.");
    }
    // 수량 확인 및 증감
    product.decreaseQuantity(request.getQuantity());
    productRepository.save(product);

    return ResponseOrderDto.builder()
            .supplierHubId(supplyCompany.getHub().getHubId())
            .recipientHubId(recipientCompany.getHub().getHubId())
            .recipientAddress(recipientCompany.getAddress())
            .build();
  }

  // company 값 읽어오기 / is_deleted = true면 조회, 수정, 삭제 불가
  private Company checkCompany(UUID companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(
            () -> new NullPointerException("해당 업체는 존재하지 않습니다.")
    );

    if (company.isDeleted()) {
      throw new IllegalArgumentException("이미 삭제 요청된 업체입니다.");
    }
    return company;
  }

  // 주문 취소 시 해당 허브에 주문 수량 반환
  public void returnQuantity(UUID productId, Integer quantity) {
    Product product = productRepository.findById(productId).orElseThrow(() ->
            new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

    product.returnQuantity(quantity);
    productRepository.save(product);
  }

}
