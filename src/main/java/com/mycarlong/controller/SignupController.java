package com.mycarlong.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycarlong.mycarlongback.dto.ApiResponse;
import com.mycarlong.mycarlongback.dto.SignupRequest;

@RestController
public class SignupController {
  @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // 여기서부터는 받은 데이터를 활용하여 회원가입 로직을 수행
        // 데이터베이스에 새로운 사용자 정보를 저장하고 등등의 작업을 수행
        System.out.println("singupcontroller도착");
        // 회원가입이 성공하면 적절한 응답을 클라이언트에게 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "회원가입이 완료되었습니다."));
    }
}
