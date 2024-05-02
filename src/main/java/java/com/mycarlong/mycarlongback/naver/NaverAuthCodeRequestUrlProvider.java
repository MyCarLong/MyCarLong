package java.com.mycarlong.mycarlongback.naver;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.mycarlong.mycarlongback.oauth.AuthCodeRequestUrlProvider;
import com.mycarlong.mycarlongback.oauth.OauthServerType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaverAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final NaverOauthConfig naverOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.NAVER;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", naverOauthConfig.clientId())
                .queryParam("redirect_uri", naverOauthConfig.redirectUri())
                .queryParam("state", naverOauthConfig.state()) // 이건 나중에 따로 찾아보고 설정해서 쓰세용!
                .build()
                .toUriString();
    }
}