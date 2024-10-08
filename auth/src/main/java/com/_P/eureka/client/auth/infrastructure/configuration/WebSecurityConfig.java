package com._P.eureka.client.auth.infrastructure.configuration;

import com._P.eureka.client.auth.infrastructure.jwt.JwtAuthenticationFilter;
import com._P.eureka.client.auth.infrastructure.jwt.JwtAuthorizationFilter;
import com._P.eureka.client.auth.infrastructure.jwt.JwtUtil;
import com._P.eureka.client.auth.infrastructure.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        return http.csrf(AbstractHttpConfigurer::disable).

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auth/**").permitAll() // '/api/auth/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers("/api/master/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI 경로 접근 허용
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api-test/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        )


        // 필터 관리
        // 인증전에 인가필터를 설정
        .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)


        .build();
    }

}

