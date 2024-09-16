package com._P.eureka.client.object.product.repository;


import com._P.eureka.client.object.company.entity.Company;
import com._P.eureka.client.object.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByCompanyAndProductName(Company company, String productName);
    Page<Product> findAllByIsDeletedFalse(Pageable pageable);
    Page<Product> findByProductNameAndIsDeletedFalse(String searchValue, Pageable pageable);
}
