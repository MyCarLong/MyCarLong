package com.mycarlong.oauth;
import static java.util.Locale.ENGLISH;

public enum OauthServerType {

    KAKAO,
    NAVER,
    GOOGLE
    ;

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}