package java.com.mycarlong.mycarlongback.config;


import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mycarlong.mycarlongback.dto.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);
    private final JWTUtil jwtUtil;


    // JWTUtil을 주입받는 생성자
    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws  ServletException, IOException {

        // OAuth2User로 캐스팅하여 사용자 정보 획득
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        logger.info("authentication {}",authentication);
        String username = customUserDetails.getUsername(); // 사용자 이름 획득



        logger.info("customUserDetails!!!! : {}",customUserDetails);
        logger.info("customUserDetails . getName() !!!! : {}",customUserDetails.getName());
        logger.info("username!!!!{}", username);
        // 사용자 권한 정보 획득
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority(); // 권한 획득

        // JWT 토큰 생성 (유효기간: 60시간)
        String token = jwtUtil.createJwt(username, role, 1000 * 60 * 60L);
        String rfToken = jwtUtil.createRefreshToken(username, role, 1000 * 60 * 10 * 60L);

        // 생성된 JWT 토큰을 쿠키에 추가하여 클라이언트로 전달
        response.addCookie(createCookie("Authorization", token));
        response.addCookie(createCookie("RefreshToken", rfToken));


        // 클라이언트를 리다이렉트하여 홈페이지로 이동
        response.sendRedirect("/userInfo");
    }

    // 쿠키 생성 메서드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60); // 쿠키 유효기간: 60시간
        cookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        cookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근할 수 없도록 설정
        return cookie;
    }
}
