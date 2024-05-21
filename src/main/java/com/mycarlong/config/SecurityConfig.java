package com.mycarlong.config;


import java.util.Collections;

import com.mycarlong.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.mycarlong.filter.JWTFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final UserService userService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    private final JWTFilter jwtFIlter;


    public SecurityConfig(UserService userService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil, JWTFilter jwtFIlter) {
        this.userService = userService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.jwtFIlter = jwtFIlter;
    }

    // SecurityFilterChain을 생성하는 메서드입니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {

        // CORS 설정
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                return configuration;
            }
        }));

        // CSRF 비활성화
        http.csrf((csrf) -> csrf.disable());

        // HTTP Basic 인증 비활성화
        http.httpBasic((httpBasic) -> httpBasic.disable());

        // JWT 필터 추가
        http.addFilterAfter(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
    /*    // OAuth2 설정
        http.oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
//                .defaultSuccessUrl("/testCookie")
        );*/

        // 경로별 인가 작업
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                .requestMatchers("/",",/aboutus","/api/**","oauth/**").permitAll()
                .anyRequest().hasRole("USER") // 나머지 요청은 인증된 사용자만 허용
        );

        // 세션 관리 설정: STATELESS로 설정하여 세션을 사용하지 않습니다.
        http.sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

}
