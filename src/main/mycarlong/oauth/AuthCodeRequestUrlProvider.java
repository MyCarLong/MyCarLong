package java.com.mycarlong.oauth;

public interface AuthCodeRequestUrlProvider {

  OauthServerType supportServer();

  String provide();
}
