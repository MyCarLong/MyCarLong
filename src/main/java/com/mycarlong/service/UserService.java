package com.mycarlong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycarlong.config.JWTUtil;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.repository.UserRepository;

@Service
public class UserService {
     @Autowired
    private UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.expiration}")
    private Long expirationTime; // 토큰의 만료 시간을 담을 변수

    @Autowired
    private JWTUtil jwtUtil; // JWTUtil 클래스의 Autowired를 통해 주입

    public String registerUser(String name, String email, String password, String contact) {
    // 새로운 사용자 생성
    UserEntity user = new UserEntity();
    if (userRepository.existsByEmail(email)) {
        System.out.println("중복된 이메일입니다.");
        throw new UsernameNotFoundException("중복된 이메일입니다.");
    } else {
        String role = "ROLE_USER";
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setContact(contact);
        user.setRole(role);

        // 데이터베이스에 사용자 정보 저장
        user = userRepository.save(user);

        // 사용자 정보를 기반으로 JWT 토큰 생성
        System.out.println("userService username :" + user.getName());
        String token = jwtUtil.createJwt(user.getEmail(), user.getRole(), expirationTime); // 여기에 만료 시간을 넣어주세요.
        System.out.println("jwt token = " + token);
        return token;
    }
}

    // 다른 사용자 관련 기능들도 추가할 수 있음
}