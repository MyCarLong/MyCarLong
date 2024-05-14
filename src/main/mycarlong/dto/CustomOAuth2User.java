package java.com.mycarlong.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    // 생성자에서 UserDTO를 받아서 저장합니다.
    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    // OAuth2User 인터페이스의 메서드를 구현합니다.

    @Override
    public Map<String, Object> getAttributes() {
        // 사용자 속성 정보를 반환합니다. 여기서는 사용하지 않으므로 null을 반환합니다.
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한 정보를 반환합니다. 여기서는 UserDTO에 저장된 권한 정보를 사용합니다.
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        // 사용자의 이름을 반환합니다.
        return userDTO.getName();
    }

    // 사용자의 유저네임을 반환합니다.
    public String getUsername() {
        return userDTO.getUsername();
    }

}
