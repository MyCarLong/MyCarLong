package com.mycarlong.controller;

import com.mycarlong.config.JwtTokenProvider;
import com.mycarlong.dto.LoginRequest;
import com.mycarlong.repository.UserRepository;
import com.mycarlong.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@RestController
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${spring.jwt.secret}")
    private String jwtSecret;
    @Autowired
    private UserService userService;

/*    @Operation(summary = "로그인", description = "사용자가 로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "이미 로그인되어 있습니다."),
            @ApiResponse(responseCode = "401", description = "이메일이나 비밀번호가 올바르지 않습니다.")
    })
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
                            new com.mycarlong.dto.ApiResponse(true, "로그인 성공", user.getName(), token));
                } else {
                    // 비밀번호가 일치하지 않으면 로그인 실패를 반환합니다.
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 올바르지 않습니다.");
                }
            }
        }
    }*/

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // authenticateAndGenerateToken 메서드에서 사용자 정보를 함께 반환하도록 수정
            UserDetails userDetails = userService.authenticateAndGenerateToken(loginRequest.getEmail(), loginRequest.getPassword());
            String token = userService.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString());
            String authority = userDetails.getAuthorities().stream().findFirst().get().toString();
            log.info("authority {}", authority);
            return ResponseEntity.status(HttpStatus.OK).body(new com.mycarlong.dto.ApiResponse(true, "로그인 성공", userDetails.getUsername(), token, authority));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 올바르지 않습니다.");
        }
    }

    @Operation(summary = "로그인된 사용자 정보", description = "로그인된 사용자의 이름을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/loggedinserinfo")
    public ResponseEntity<?> getLoggenInUserName(@AuthenticationPrincipal Object principal) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String loggedinUsername = authentication.getName();
//        Object loggedinUsername =  principal;
        logger.info("loggedinUsername {}", loggedinUsername);
        return new ResponseEntity<>(loggedinUsername,HttpStatus.OK);
    }

    @Operation(summary = "서버 상태 확인", description = "서버가 동작 중인지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버가 정상적으로 동작 중입니다.")
    })
    @GetMapping("/isServerOn")
    public ResponseEntity<String> getLoggenInUserName() {
        return ResponseEntity.ok().body("ok");
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