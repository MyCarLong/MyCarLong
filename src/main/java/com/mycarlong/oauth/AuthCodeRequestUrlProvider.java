package com.mycarlong.oauth;

public interface AuthCodeRequestUrlProvider {

  OauthServerType supportServer();

  String provide();
}
