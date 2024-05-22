package com.mycarlong.oauth;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeRequestUrlProviderComposite {

    // OauthServerType과 AuthCodeRequestUrlProvider의 매핑을 저장하는 맵
    private final Map<OauthServerType, AuthCodeRequestUrlProvider> mapping;

    // 생성자를 통해 mapping 맵을 초기화
    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
        mapping = providers.stream()
                .collect(toMap(
                        AuthCodeRequestUrlProvider::supportServer, // 각 provider가 지원하는 서버 타입을 키로 사용
                        identity() // provider 자신을 값으로 사용
                ));
    }

    // 특정 OauthServerType에 대한 요청 URL을 제공
    public String provide(OauthServerType oauthServerType) {
        return getProvider(oauthServerType).provide();
    }

    // 특정 OauthServerType을 지원하는 provider를 반환
    private AuthCodeRequestUrlProvider getProvider(OauthServerType oauthServerType) {
        // 해당하는 provider가 없으면 예외를 발생시킴
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다."));
    }
}
