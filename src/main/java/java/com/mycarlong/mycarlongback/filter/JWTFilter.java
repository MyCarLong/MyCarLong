package java.com.mycarlong.mycarlongback.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mycarlong.mycarlongback.config.JWTUtil;
import com.mycarlong.mycarlongback.dto.CustomOAuth2User;
import com.mycarlong.mycarlongback.dto.UserDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 모든 요청에 대해 단 한 번 실행되는 필터 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에서 인증 정보를 추출
        String authorization = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        // 쿠키가 있는지 확인
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
//                    logger.info("Authorization : " + authorization);
                }else if(cookie.getName().equals("RefreshToken")){
                    refreshToken = cookie.getValue();
//                    logger.info("RefreshToken : " + refreshToken);
                }
            }
        } else {
            filterChain.doFilter(request, response);
            return;
        }



        // Authorization 헤더가 없는 경우 필터 체인 진행
        if (authorization == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        } else if (refreshToken!=null) {
            if(jwtUtil.validateRefreshToken(refreshToken)){
                filterChain.doFilter(request, response);
                return;
            }else {
                String username = null;
                String role = null;
                try {
                    // JWT 토큰 추출
                    String token = authorization;

                    // 토큰에서 사용자 이름과 역할 추출
                    username = jwtUtil.getUsername(token);
                    role = jwtUtil.getRole(token);
                    if (username==null || role==null) {
                        String cause = username!=null? username: role;
                        throw new IllegalArgumentException(cause);
                    }
                    logger.info("username : " + username + " role : " + role);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }

                if(username.equals(jwtUtil.getUsername(refreshToken)) && role.equals(jwtUtil.getRole(refreshToken))) {
                    ReAuth(username, role);
                }

            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private static void ReAuth(String username, String role) {
        // UserDTO를 생성하고 값 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        // CustomOAuth2User를 사용하여 사용자 정보 객체 생성
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
