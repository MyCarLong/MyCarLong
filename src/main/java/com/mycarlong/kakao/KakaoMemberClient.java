package com.mycarlong.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.mycarlong.oauth.OauthMember;
import com.mycarlong.oauth.OauthMemberClient;
import com.mycarlong.oauth.OauthServerType;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    // 제공된 인증 코드를 사용하여 OAuth 멤버 정보를 가져옴
    @Override
    public OauthMember fetch(String authCode) {
        // 1단계: 인증 코드를 사용하여 토큰을 가져옴
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode)); // (1)
        // 2단계: 액세스 토큰을 사용하여 사용자 정보를 가져옴
        KakaoMemberResponse kakaoMemberResponse =
                kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());  // (2)
        // 3단계: KakaoMemberResponse를 도메인 객체로 변환하여 반환
        return kakaoMemberResponse.toDomain();  // (3)
    }

    // 토큰 요청 파라미터를 생성
    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }
}
