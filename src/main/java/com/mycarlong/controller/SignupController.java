package com.mycarlong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycarlong.mycarlongback.dto.ApiResponse;
import com.mycarlong.mycarlongback.dto.SignupRequest;
import com.mycarlong.mycarlongback.entity.UserEntity;
import com.mycarlong.mycarlongback.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class SignupController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // 회원가입 로직 호출
        UserEntity savedUser = userService.registerUser(signupRequest.getName(), signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getContact());
        System.out.println("가입자 = " + savedUser.getName());
        if(savedUser.getEmail() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 이메일입니다.");
        }else{

            // 회원가입이 성공하면 적절한 응답을 클라이언트에게 전송
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "회원가입이 완료되었습니다."));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().body("로그아웃 되었습니다.");

    }
    
}
