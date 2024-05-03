package com.mycarlong.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycarlong.mycarlongback.oauth.OauthMember;
import com.mycarlong.mycarlongback.oauth.OauthMemberClient;
import com.mycarlong.mycarlongback.oauth.OauthServerType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    public OauthMember fetch(String authCode) {
        GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode)); // (1)
        // String token = tokenInfo.id_token();
        // DecodedJWT jwt = JWT.decode(token);
        // System.out.println("Header = " + jwt.getHeader());
        // System.out.println("Payload = " + jwt.getPayload());
        // System.out.println("Signature = " + jwt.getSignature());
        // System.out.println("name,email = " + jwt.getClaim("name") + jwt.getClaim("email"));
        GoogleMemberResponse googleMemberResponse =
                googleApiClient.fetchMember("Bearer " + tokenInfo.access_token());  // (2)
                logger.info("access_token {}", tokenInfo.access_token());
        return googleMemberResponse.toDomain();  // (3)
    }

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