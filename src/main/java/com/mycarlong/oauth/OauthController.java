package com.mycarlong.oauth;

import org.eclipse.jdt.internal.compiler.codegen.ObjectCache;
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

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<?> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ) {
//        Long login = oauthService.login(oauthServerType, code);
        Map<String, Object> loginInfo = oauthService.login(oauthServerType, code);
        Long login = (Long) loginInfo.get("id");
        String nickname = (String) loginInfo.get("nickname");
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("login",login,"nickname",nickname));
    }
}