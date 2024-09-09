package com._P.eureka.client.auth.infrastructure.jwt;

import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

// Util클래스란 ?
// 특정한 매개변수나,파라미터에 대한 작업을 수행하는 메서드들이 존재하는 클래스
// 다른객체에 의존하지 않고 하나의 모듈로서 동작하는 클래스
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    // role 별 secretKey
    @Value("${key.company}") // Base64 Encode 한 SecretKey
    private String COMPANY_KEY;

    @Value("${key.delivery_person}")
    private String DELIVERY_PERSON_KEY;

    @Value("${key.master}")
    private String MASTER_KEY;

    @Value("${key.hub_manager}")
    private String HUB_MANAGER_KEY;

    private Key keyCompany;
    private Key keyDeliveryPerson;
    private Key keyMaster;
    private Key keyHubManager;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        keyCompany = Keys.hmacShaKeyFor(Base64.getDecoder().decode(COMPANY_KEY));
        keyDeliveryPerson = Keys.hmacShaKeyFor(Base64.getDecoder().decode(DELIVERY_PERSON_KEY));
        keyMaster = Keys.hmacShaKeyFor(Base64.getDecoder().decode(MASTER_KEY));
        keyHubManager = Keys.hmacShaKeyFor(Base64.getDecoder().decode(HUB_MANAGER_KEY));
    }

    // JWT 생성, 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();
        Key key = getKeyByRole(role);

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한 (claim : key-value로 넣을수있다.)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // role에 따라 key 반환하는 메서드
    private Key getKeyByRole(UserRoleEnum role) {
        switch (role) {
            case COMPANY:
                return keyCompany;
            case DELIVERY_PERSON:
                return keyDeliveryPerson;
            case MASTER:
                return keyMaster;
            case HUB_MANAGER:
                return keyHubManager;
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }


    // 생성된 JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // Cookie에 들어있던 JWT 토큰을 Substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token, UserRoleEnum role) {
        try {
            Jwts.parserBuilder().setSigningKey(getKeyByRole(role))
                    .build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // JWT 에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token, UserRoleEnum role) {
        return Jwts.parserBuilder().setSigningKey(getKeyByRole(role))
                .build().parseClaimsJws(token).getBody();
    }

    // header에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
