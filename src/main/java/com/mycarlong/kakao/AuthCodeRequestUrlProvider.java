package com.mycarlong.kakao;


public interface AuthCodeRequestUrlProvider {

  OauthServerType supportServer();

  String provide();
}
