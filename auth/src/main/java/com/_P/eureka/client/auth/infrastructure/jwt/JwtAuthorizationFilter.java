package com._P.eureka.client.auth.infrastructure.jwt;

import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import com._P.eureka.client.auth.infrastructure.security.UserDetailsImpl;
import com._P.eureka.client.auth.infrastructure.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        // 1. JWT 토큰을 요청 헤더에서 가져온다.
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        // 2. 토큰이 존재할 경우
        if (StringUtils.hasText(tokenValue)) {
            // 'auth' 헤더에서 역할 정보를 가져옴
            String authHeader = req.getHeader("auth");
            if (authHeader == null) {
                log.error("Missing 'auth' header in request");
                filterChain.doFilter(req, res);
                return;
            }

            UserRoleEnum role;
            try {
                role = UserRoleEnum.valueOf(authHeader); // 역할 변환 시도
            } catch (IllegalArgumentException e) {
                log.error("Invalid role in 'auth' header: {}", authHeader);
                filterChain.doFilter(req, res);
                return;
            }

            // jwt 토큰이 유효한지 검사 -> 토큰과 권한을 함께 사용하여 검증한다.
            if (!jwtUtil.validateToken(tokenValue, role)) {
                log.error("Token Error");
                return;
            }

            // jwt 토큰에서 사용자 정보(claims)를 추출한다.
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue, role);

            try {
                // 추출한 사용자 ID(info.getSubject())로 인증을 설정
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // 필터 체인의 다음 필터를 호출하여 요청과 응답을 전달
        filterChain.doFilter(req, res);


    }

    // 인증 처리
    public void setAuthentication(String username) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        Authentication authentication = createAuthentication(username);
//        context.setAuthentication(authentication);
//
//        SecurityContextHolder.setContext(context);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);


        System.out.println("UserDetails: " + userDetails.getUsername());
        System.out.println("Authorities: " + userDetails.getAuthorities());


        // UserDetailsImpl이 사용되도록 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

