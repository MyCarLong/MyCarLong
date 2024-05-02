package java.com.mycarlong.mycarlongback.dto;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    // 생성자에서 OAuth2 로그인 응답의 속성을 매핑합니다.
    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    // OAuth2Response 인터페이스의 메서드를 구현합니다.

    @Override
    public String getProvider() {
        // 제공자를 나타내는 문자열 반환
        return "google";
    }

    @Override
    public String getProviderId() {
        // 제공자의 사용자 ID를 반환
        return attribute.get("sub").toString();
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
