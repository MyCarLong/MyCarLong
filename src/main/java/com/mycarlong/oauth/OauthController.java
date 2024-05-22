package com.mycarlong.oauth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;

    // 인증 코드 요청 URL로 리다이렉트하는 엔드포인트
    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        // 주어진 OAuth 서버 타입에 따라 인증 코드 요청 URL을 가져옴
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        // 해당 URL로 리다이렉트
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    // 로그인 엔드포인트
    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<?> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ) {
        // 주어진 OAuth 서버 타입과 인증 코드를 사용하여 로그인 처리
        Map<String, Object> loginInfo = oauthService.login(oauthServerType, code);
        // 로그인 ID와 닉네임을 가져옴
        Long login = (Long) loginInfo.get("id");
        String nickname = (String) loginInfo.get("nickname");
        // 로그인 정보와 닉네임을 응답 본문에 포함하여 반환
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("login", login, "nickname", nickname));
    }
}
