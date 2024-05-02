package com.mycarlong.kakao;

public interface OauthMemberClient {

  OauthServerType supportServer();

  OauthMember fetch(String code);
}
