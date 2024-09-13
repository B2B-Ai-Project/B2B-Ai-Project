package com._P.eureka.client.object.company.repository;

import com._P.eureka.client.object.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Page<Company> findByNameContainingIgnoreCase(String searchValue, Pageable pageable);
    Optional<Company> findByNameAndAddress(String name, String address);
    Page<Company> findAll(Pageable pageable);

    Page<Company> findByCompanyTypeContainingIgnoreCase(String searchValue, Pageable pageable);
}
