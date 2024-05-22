package com.mycarlong.oauth;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberClientComposite {

    // OauthServerType과 OauthMemberClient의 매핑을 저장하는 맵
    private final Map<OauthServerType, OauthMemberClient> mapping;

    // 생성자를 통해 mapping 맵을 초기화
    public OauthMemberClientComposite(Set<OauthMemberClient> clients) {
        mapping = clients.stream()
                .collect(toMap(
                        OauthMemberClient::supportServer, // 각 클라이언트가 지원하는 서버 타입을 키로 사용
                        identity() // 클라이언트 자신을 값으로 사용
                ));
    }

    // 특정 OauthServerType에 대한 인증 코드를 사용하여 OauthMember 정보를 가져옴
    public OauthMember fetch(OauthServerType oauthServerType, String authCode) {
        return getClient(oauthServerType).fetch(authCode);
    }

    // 특정 OauthServerType을 지원하는 클라이언트를 반환
    private OauthMemberClient getClient(OauthServerType oauthServerType) {
        // 해당하는 클라이언트가 없으면 예외를 발생시킴
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다."));
    }
}
