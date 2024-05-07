package com.mycarlong.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycarlong.dto.ApiResponse;
import com.mycarlong.dto.SignupRequest;

@RestController
public class SignupController {
  @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // SignupRequest는 클라이언트에서 전송한 회원가입 데이터를 담는 자바 객체입니다.
        // 이 객체의 필드들은 클라이언트에서 전송한 JSON 데이터와 매핑됩니다.
        
        // 여기서부터는 받은 데이터를 활용하여 회원가입 로직을 수행
        // 데이터베이스에 새로운 사용자 정보를 저장하고 등등의 작업을 수행
        
        // 회원가입이 성공하면 적절한 응답을 클라이언트에게 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "회원가입이 완료되었습니다."));
    }
}
