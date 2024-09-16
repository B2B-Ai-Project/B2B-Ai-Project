package com._P.eureka.client.object.product.controller;


import com._P.eureka.client.object.product.dto.ProductCreateDto;
import com._P.eureka.client.object.product.dto.ProductResponseDto;
import com._P.eureka.client.object.product.dto.ProductUpdateDto;
import com._P.eureka.client.object.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {
    // TODO
    // User 검증

    private final ProductService productService;

    @PostMapping
    public String create(@RequestBody ProductCreateDto requestDto) {

        return productService.create(requestDto);
    }

    @PutMapping("{product_id}")
    public ProductResponseDto update(@RequestBody ProductUpdateDto requestDto, @PathVariable(name = "product_id") UUID productId) {

        return productService.update(requestDto, productId);
    }

    @DeleteMapping("{product_id}")
    public String delete(@PathVariable (name = "product_id") UUID productId){

        return productService.delete(productId);
    }

    // ## 모든 로그인 사용자가 가능
    @GetMapping("{product_id}")
    public ProductResponseDto getOne(@PathVariable(name = "product_id") UUID productId) {
        return productService.getOne(productId);

    }

    // 제품 이름으로 검색
    @GetMapping
    public Page<ProductResponseDto> search(
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return productService.search(searchValue, pageable);
    }
}
