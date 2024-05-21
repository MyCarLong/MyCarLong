package com.mycarlong.naver;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.mycarlong.oauth.OauthMember;
import com.mycarlong.oauth.OauthMemberClient;
import com.mycarlong.oauth.OauthServerType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaverMemberClient implements OauthMemberClient {

    private final NaverApiClient naverApiClient;
    private final NaverOauthConfig naverOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.NAVER;
    }

    // 제공된 인증 코드를 사용하여 OAuth 멤버 정보를 가져옴
    @Override
    public OauthMember fetch(String authCode) {
        // 1단계: 인증 코드를 사용하여 토큰을 가져옴
        NaverToken tokenInfo = naverApiClient.fetchToken(tokenRequestParams(authCode));
        // 2단계: 액세스 토큰을 사용하여 사용자 정보를 가져옴
        NaverMemberResponse naverMemberResponse = naverApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        System.out.println("naverMemberResponse = " + naverMemberResponse);
        // 3단계: NaverMemberResponse를 도메인 객체로 변환하여 반환
        return naverMemberResponse.toDomain();
    }

    // 토큰 요청 파라미터를 생성
    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverOauthConfig.clientId());
        params.add("client_secret", naverOauthConfig.clientSecret());
        params.add("code", authCode);
        params.add("state", naverOauthConfig.state());
        return params;
    }
}
