package com.mycarlong.controller;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycarlong.mycarlongback.dto.ApiResponse;
import com.mycarlong.mycarlongback.dto.LoginRequest;
import com.mycarlong.mycarlongback.entity.UserEntity;
import com.mycarlong.mycarlongback.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 이미 인증된 사용자일 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 로그인되어 있습니다.");
        } else {
            // 입력된 이메일로 사용자를 찾습니다.
            UserEntity user = userRepository.findByEmail(loginRequest.getEmail());

            if (user == null) {
                // 사용자가 존재하지 않으면 로그인 실패를 반환합니다.
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 올바르지 않습니다.");
            } else {
                // 사용자가 존재하면 입력된 비밀번호와 저장된 비밀번호를 비교합니다.
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    // 비밀번호가 일치하면 새로운 토큰을 발행합니다.
                    String token = generateToken(user.getEmail());
                    // 발행된 토큰과 사용자의 이름을 함께 반환합니다.
                    return ResponseEntity.ok().body(
                            new ApiResponse(true, "로그인 성공", user.getName(), token));
                } else {
                    // 비밀번호가 일치하지 않으면 로그인 실패를 반환합니다.
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 올바르지 않습니다.");
                } 
            }
        }
    }

    private String generateToken(String subject) {
      // 이전 토큰이 만료되도록 만료 시간을 짧게 설정합니다.
      long expirationTime = 60_000; // 1분 (단위: 밀리초)
      Date now = new Date();
      Date expirationDate = new Date(now.getTime() + expirationTime);
  
      // 시크릿 키를 생성합니다.
      SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
      // 토큰을 생성하여 반환합니다.
      return Jwts.builder()
              .setSubject(subject)
              .setExpiration(expirationDate)
              .signWith(secretKey, SignatureAlgorithm.HS512)
              .compact();
  }
}
