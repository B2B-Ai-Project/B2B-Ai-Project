package com._P.eureka.client.gateway.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        log.info("Gateway Routes 설정됨");
        return builder.routes()
                .route("ai-service", r -> r.path("/ai/**")
                        .uri("lb://ai-service"))
                .route("auth-service", r -> r.path("/auth/**", "/api/master/**")
                        .uri("lb://auth-service"))
                .route("delivery-service", r -> r.path("/delivery/**")
                        .uri("lb://delivery-service"))
                .route("object-service", r -> r.path("/object/**")
                        .uri("lb://object-service"))
                .route("order-service", r -> r.path("/order/**")
                        .uri("lb://order-service"))
                .build();
    }
}



