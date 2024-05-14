package java.com.mycarlong.oauth;

public interface OauthMemberClient {

  OauthServerType supportServer();

  OauthMember fetch(String code);
}
