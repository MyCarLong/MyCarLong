package com.mycarlong.oauth;
import static java.util.Locale.ENGLISH;

public enum OauthServerType {
//다른 소셜로그인 추가가능
    KAKAO,
    NAVER,
    GOOGLE,
    ;

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}