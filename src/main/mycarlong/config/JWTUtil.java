package java.com.mycarlong.config;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {

    // 시크릿 키를 저장하는 변수
    private SecretKey secretKey;
    private Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    // 생성자를 통해 시크릿 키를 주입받아 SecretKey를 생성합니다.
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        // 주입받은 시크릿 키를 사용하여 SecretKey를 생성합니다.
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 주어진 토큰에서 사용자 이름을 추출하여 반환합니다.
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    // 주어진 토큰에서 사용자 역할을 추출하여 반환합니다.
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // 주어진 리프레시 토큰이 유효한지 확인합니다.
    public Boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid refresh token.", e);
            return false;
        }
    }



    // 주어진 토큰이 만료되었는지 여부를 확인합니다.
    public Boolean isExpired(String token) {
//        logger.info(String.valueOf(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date())));
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 주어진 사용자 이름, 역할 및 만료 시간을 기반으로 JWT를 생성합니다.
    public String createJwt(String username, String role, Long expiredMs) {
//        logger.info(String.valueOf(System.currentTimeMillis()));
//        logger.info(String.valueOf(System.currentTimeMillis() + expiredMs));
        return Jwts.builder()
                .claim("username", username) // 사용자 이름을 클레임에 추가합니다.
                .claim("role", role) // 사용자 역할을 클레임에 추가합니다.
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간을 설정합니다.
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰 만료 시간을 설정합니다.
                .signWith(secretKey) // 시크릿 키를 사용하여 토큰에 서명합니다.
                .compact(); // 토큰을 생성하고 문자열로 반환합니다.
    }

    // 주어진 사용자 이름, 역할 및 만료 시간을 기반으로 리프레시 토큰을 생성합니다.
    public String createRefreshToken(String username, String role, Long expiredMs) {
//        logger.info(String.valueOf(System.currentTimeMillis()));
//        logger.info(String.valueOf(System.currentTimeMillis() + expiredMs));
        return Jwts.builder()
                .claim("username", username) // 사용자 이름을 클레임에 추가합니다.
                .claim("role", role) // 사용자 역할을 클레임에 추가합니다.
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간을 설정합니다.
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰 만료 시간을 설정합니다.
                .signWith(secretKey) // 시크릿 키를 사용하여 토큰에 서명합니다.
                .compact(); // 토큰을 생성하고 문자열로 반환합니다.
    }
}
