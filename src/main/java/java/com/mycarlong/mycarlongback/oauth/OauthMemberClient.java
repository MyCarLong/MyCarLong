package java.com.mycarlong.mycarlongback.oauth;

public interface OauthMemberClient {

  OauthServerType supportServer();

  OauthMember fetch(String code);
}
