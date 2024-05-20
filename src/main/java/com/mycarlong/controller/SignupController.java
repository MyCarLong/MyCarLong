package com.mycarlong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycarlong.dto.ApiResponse;
import com.mycarlong.dto.SignupRequest;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class SignupController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    try {
        // 회원가입 로직 호출
        String token = userService.registerUser(signupRequest.getName(), signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getContact());
        System.out.println("jwt token = " + token);
        System.out.println("사용자 = " + signupRequest.getName());
        // 회원가입이 성공하면 적절한 응답을 클라이언트에게 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "회원가입이 완료되었습니다.",signupRequest.getName() ,token));
    } catch (UsernameNotFoundException e) {
        // 중복된 이메일이 있을 경우 예외 처리하여 적절한 응답 반환
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 이메일입니다.");
    }
}

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().body("로그아웃 되었습니다.");

    }
    
}
