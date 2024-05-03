package com.mycarlong.google;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.mycarlong.mycarlongback.oauth.AuthCodeRequestUrlProvider;
import com.mycarlong.mycarlongback.oauth.OauthServerType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

  private final GoogleOauthConfig googleOauthConfig;

  @Override
  public OauthServerType supportServer() {
    return OauthServerType.GOOGLE;
  }

  @Override
  public String provide() {
    return UriComponentsBuilder
            .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
            .queryParam("response_type", "code")
            .queryParam("client_id", googleOauthConfig.clientId())
            .queryParam("redirect_uri", googleOauthConfig.redirectUri())
            .queryParam("scope", String.join(" ", googleOauthConfig.scope()))
            .toUriString();
  }
  
}
