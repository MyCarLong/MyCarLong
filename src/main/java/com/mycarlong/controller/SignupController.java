package com.mycarlong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycarlong.dto.ApiResponse;
import com.mycarlong.dto.SignupRequest;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.repository.UserRepository;

@RestController
public class SignupController {
  @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // 회원가입 로직 수행
        UserEntity user = new UserEntity();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setContact(signupRequest.getContact());
        user.setRole("ROLE_USER");

        // 데이터베이스에 사용자 정보 저장
        UserEntity savedUser = userRepository.save(user);
        System.out.println("가입자 = " + savedUser.getName());
        // 회원가입이 성공하면 적절한 응답을 클라이언트에게 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "회원가입이 완료되었습니다."));
    }
}
