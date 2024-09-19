package com._P.eureka.client.auth.infrastructure.jwt;

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
import java.util.Arrays;
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
        try {
            // 디코딩 전 실제 Base64문자열을 인쇄하는 로깅을 추가하여 확인
            logger.info("Master Key Base64 : {}", MASTER_KEY);

            keyCompany = Keys.hmacShaKeyFor(Base64.getDecoder().decode(COMPANY_KEY));
            keyDeliveryPerson = Keys.hmacShaKeyFor(Base64.getDecoder().decode(DELIVERY_PERSON_KEY));
            keyMaster = Keys.hmacShaKeyFor(Base64.getDecoder().decode(MASTER_KEY));
            keyHubManager = Keys.hmacShaKeyFor(Base64.getDecoder().decode(HUB_MANAGER_KEY));

            // 디코딩 직후 로깅추가하여 키가 올바르게 변환되었는지 확인
            byte[] decodedKey = Base64.getDecoder().decode(MASTER_KEY);
            logger.info("decoded key : {}", Arrays.toString(decodedKey));


            // 각 키들이 제대로 설정되었는지 로그로 확인
            logger.info("Company Key: {}", keyCompany);
            logger.info("Delivery Person Key: {}", keyDeliveryPerson);
            logger.info("Master Key: {}", keyMaster);
            logger.info("Hub Manager Key: {}", keyHubManager);

            logger.info("Signing keys initialized successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Error initializing signing keys: {}", e.getMessage());
        }
    }

    // role에 따라 key 반환하는 메서드
    public Key getKeyByRole(UserRoleEnum role) {
        Key key;  // 선언된 변수를 사용하여 key 값을 할당합니다.
        switch (role) {
            case COMPANY:
                key = keyCompany;
                break;
            case DELIVERY_PERSON:
                key = keyDeliveryPerson;
                break;
            case MASTER:
                key = keyMaster;
                break;
            case HUB_MANAGER:
                key = keyHubManager;
                break;
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }




        // key와 role 정보 로그 출력
        logger.info("Selected key for role {}: {}", role, key);
        return key;  // 마지막에 할당된 key를 반환합니다.
    }


    // JWT 생성, 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();
        Key key = getKeyByRole(role);

        logger.info("Creating token for username: {}, role: {}, key: {}", username, role, key);

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한 (claim : key-value로 넣을수있다.)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
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

    // JWT에서 역할을 추출하고 적절한 서명 키를 반환하는 메서드
    private Key getKeyFromToken(String token) {
        try {
            logger.info("Extracting claims from token: {}", token);  // 토큰 확인

            // 토큰을 서명 키 없이 파싱하려고 하면 문제가 발생하므로, 먼저 서명 키를 설정
            Claims claims = Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            // 역할에 따라 적절한 서명 키를 반환
                            UserRoleEnum role = UserRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
                            logger.info("Extracted role from token: {}", role);
                            return getKeyByRole(role); // 역할에 맞는 서명 키 반환
                        }
                    })
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            UserRoleEnum role = UserRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
            return getKeyByRole(role);
        } catch (Exception e) {
            logger.error("Error extracting key from token: " + e.getMessage()); // 예외 발생 시 전체 스택 트레이스를 기록
            throw new RuntimeException("Failed to extract key from token", e);
        }
    }

    // JWT 검증
    public boolean validateToken(String token, UserRoleEnum role) {
        try {
            logger.info("Validating token: {}", token);  // 검증하려는 토큰 출력
            // 토큰에서 역할에 맞는 키를 먼저 추출
            Key key = getKeyFromToken(token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("Token validation successful");

            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
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

    // JWT 토큰에서 만료 시간 확인
    public long getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public Key resolveSigningKey(JwsHeader header, Claims claims) {
                        UserRoleEnum role = UserRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
                        return getKeyByRole(role);
                    }
                })
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 만료 시간 반환
        return claims.getExpiration().getTime();
    }

    // 만료된 토큰인지 확인하는 로직
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            UserRoleEnum role = UserRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
                            return getKeyByRole(role);
                        }
                    })
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date()); // 현재 시간과 비교하여 만료 여부 반환
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return true; // 오류가 발생하면 만료된 것으로 간주
        }
    }




}
