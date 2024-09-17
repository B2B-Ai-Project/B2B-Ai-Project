package com._P.eureka.client.object.product.repository;


import com._P.eureka.client.object.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
