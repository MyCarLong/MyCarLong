package com.mycarlong.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class LoginController {

  @Autowired
  UserRepository userRepository;


  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/api/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    // 입력된 이메일로 사용자를 찾습니다.
    UserEntity user = userRepository.findByEmail(loginRequest.getEmail());

    if (user == null) {
        // 사용자가 존재하지 않으면 로그인 실패를 반환합니다.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 올바르지 않습니다.");
    } else {
        // 사용자가 존재하면 입력된 비밀번호와 저장된 비밀번호를 비교합니다.
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // 비밀번호가 일치하면 로그인 성공을 반환합니다.
            return ResponseEntity.ok().body(new ApiResponse(true, "로그인 성공",user.getName()));
        } else {
            // 비밀번호가 일치하지 않으면 로그인 실패를 반환합니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 올바르지 않습니다.");
        }
    }
}
}
