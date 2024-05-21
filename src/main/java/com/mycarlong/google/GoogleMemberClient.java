package com.mycarlong.google;

import com.mycarlong.oauth.OauthMember;
import com.mycarlong.oauth.OauthMemberClient;
import com.mycarlong.oauth.OauthServerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OauthMemberClient {

    private final GoogleApiClient googleApiClient;
    private final GoogleOauthConfig googleOauthConfig;

    private Logger logger = LoggerFactory.getLogger(GoogleMemberClient.class);

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.GOOGLE;
    }

    // 제공된 인증 코드를 사용하여 OAuth 멤버 정보를 가져옴
    @Override
    public OauthMember fetch(String authCode) {
        // 1단계: 인증 코드를 사용하여 토큰을 가져옴
        GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode)); // (1)
        // 2단계: 액세스 토큰을 사용하여 사용자 정보를 가져옴
        GoogleMemberResponse googleMemberResponse =
                googleApiClient.fetchMember("Bearer " + tokenInfo.access_token());  // (2)
        logger.info("access_token {}", tokenInfo.access_token());
        // 3단계: GoogleMemberResponse를 도메인 객체로 변환하여 반환
        return googleMemberResponse.toDomain();  // (3)
    }

    // 토큰 요청 파라미터를 생성
    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleOauthConfig.clientId());
        params.add("redirect_uri", googleOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", googleOauthConfig.clientSecret());
        logger.info("params {} ", params);
        return params;
    }
}
