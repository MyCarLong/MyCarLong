package com.mycarlong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mycarlong.oauth.OauthServerTypeConverter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(
						"https://openapi.naver.com/v1/nid/me",
						"https://openapi.naver.com/**",
						"https://nid.naver.com/oauth2.0/token",
						"https://nid.naver.com/**",
						"https://*.naver.com/**",
						"https://www.googleapis.com/userinfo/v2/me",
						"https://www.googleapis.com/**",
						"https://*.googleapis.com/**",
						"https://oauth2.googleapis.com/**",
						"https://kapi.kakao.com/v2/user/me",
						"https://kapi.kakao.com/**",
						"https://*.kakao.com/**",
						"https://kauth.kakao.com/oauth/token",
						"https://kauth.kakao.com/**",
						"https://*.kakao.com/**",
						"http://localhost:3000", "http://localhost:3008",
						"http://mymcl.live", "https://mymcl.live",
						"https://www.mymcl.live")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
				.exposedHeaders("Set-Cookie", "Authorization")
				.allowCredentials(true)
				.maxAge(3600L);
	}

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
    }
}

