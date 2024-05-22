package com.mycarlong.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.mycarlong.google.GoogleApiClient;
import com.mycarlong.kakao.KakaoApiClient;
import com.mycarlong.naver.NaverApiClient;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoApiClient kakaoApiClient() {
        return createHttpInterface(KakaoApiClient.class);
    }

    @Bean
    public NaverApiClient naverApiClient() {
        return createHttpInterface(NaverApiClient.class);
    }

    @Bean
    public GoogleApiClient googleApiClient(){
        return createHttpInterface(GoogleApiClient.class);
    }

    // 주어진 인터페이스 타입에 대한 HTTP 인터페이스를 생성
    private <T> T createHttpInterface(Class<T> clazz) {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return build.createClient(clazz);
    }
}
