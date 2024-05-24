package com.mycarlong.controller;

import com.mycarlong.repository.UserRepository;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bouncycastle.openssl.PasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;


import com.mycarlong.dto.SignupRequest;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.List;


@RestController
@Tag(name = "SignupController", description = "회원 가입 및 관련 기능을 처리하는 컨트롤러")
public class SignupController {
    @Autowired
    private UserService userService;
    private final Logger logger= LoggerFactory.getLogger(SignupController.class);

    @Operation(summary = "회원 가입", description = "회원 가입 요청을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "중복된 이메일입니다.")
    })
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    try {
        // 회원가입 로직 호출
        String token = userService.registerUser(signupRequest.getName(), signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getContact());
        System.out.println("사용자 = " + signupRequest.getName());
        // 회원가입이 성공하면 적절한 응답을 클라이언트에게 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(new com.mycarlong.dto.ApiResponse(true, "회원가입이 완료되었습니다.",signupRequest.getName() ,token));
    } catch (UsernameNotFoundException e) {
        // 중복된 이메일이 있을 경우 예외 처리하여 적절한 응답 반환
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 이메일입니다.");
    }
}
    @Operation(summary = "비밀번호 비교", description = "입력된 비밀번호와 DB에 저장된 비밀번호를 비교합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정할 정보를 입력해주세요"),
            @ApiResponse(responseCode = "400", description = "잘못된 비밀번호 입니다.")
    })
    @PostMapping("/api/mypage")
    public ResponseEntity<?> passwordCompare(@RequestBody SignupRequest signupRequest) {
        String password = signupRequest.getPassword();
        String name = signupRequest.getName();
        // 받아온 이름과 비밀번호를 DB와 비교 후 회원정보 일치시 OK 불일치시 BAD_REQUEST
        String comparePassword = userService.findByPassword(password,name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggedinUsername = authentication.getName();
        logger.info("loggedinUsername {}", loggedinUsername);
//        Object loggedinUsername =  principal;
        return new ResponseEntity<>(loggedinUsername,HttpStatus.OK);
//        if(comparePassword!=null){
//            return ResponseEntity.status(HttpStatus.OK).body("수정할 정보를 입력해주세요");
//        } else{
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 비밀번호 입니다.");
//        }
    }



    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정이 완료되었습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 정보 입니다.")
    })
    @PutMapping("/api/modify")
    public ResponseEntity<?> modifyUser(@RequestBody SignupRequest signupRequest) {
        logger.info("signupRequest : {}", signupRequest.toString());
        String password = signupRequest.getPassword();
        String name = signupRequest.getName();
        String email = signupRequest.getEmail();
        String newPassword = signupRequest.getChangePassword();
        String contact = signupRequest.getContact();
        System.out.println(password+name+email+newPassword+contact);
        boolean isUpdated = userService.verifyAndUpdatePassword(name, password, email, contact, newPassword);

        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("수정이 완료되었습니다");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 정보 입니다.");
        }
        /*String modifyUser = userService.findByNamePasswordEmailContact(name,password,email,newPassword,contact);
        System.out.println("modifyUser실행" + modifyUser);
        if(modifyUser!=null){
            return ResponseEntity.status(HttpStatus.OK).body("수정이 완료되었습니다");
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 정보 입니다.");
        }*/
    }


    @Operation(summary = "로그아웃", description = "사용자를 로그아웃 시킵니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 되었습니다.")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().body("로그아웃 되었습니다.");

    }

}
