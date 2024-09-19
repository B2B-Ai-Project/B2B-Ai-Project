package com._P.eureka.client.gateway.infrastructure.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j(topic = "JwtAuthGlobalFilter 확인")
@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    public JwtAuthGlobalFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

        log.info("Extracted JWT from header: {}", token);

        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 헤더에서 Bearer 토큰 제거
         token = token.substring(7);

        log.info("Sending request to Auth service to validate token: {}", token);

        // WebClient를 사용해 Auth 서비스 호출
        // Auth 서비스로 HTTP 요청을 통해 토큰 검증 및 블랙리스트 확인
        return webClient.get()
                .uri("http://localhost:19093/api/auth/validateToken")  // Auth 서비스 API 호출
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    log.info("Token validation successful: {}", response);



                    return chain.filter(exchange); // 검증 성공 시 다음 필터로 전달
                })
                .onErrorResume(e -> {
                    log.error("Token validation failed: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }



    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

