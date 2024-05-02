package com.mycarlong.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    // 생성자에서 OAuth2 로그인 응답의 속성을 매핑합니다.
    public NaverResponse(Map<String, Object> attribute) {
        // 네이버로부터 받은 응답에서 'response' 키에 해당하는 값을 가져옵니다.
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    // OAuth2Response 인터페이스의 메서드를 구현합니다.

    @Override
    public String getProvider() {
        // 제공자를 나타내는 문자열 반환
        return "naver";
    }

    @Override
    public String getProviderId() {
        // 제공자의 사용자 ID를 반환
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        // 사용자 이메일을 반환
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        // 사용자 이름을 반환
        return attribute.get("name").toString();
    }
}
