package com.mycarlong.controller;

import com.mycarlong.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class KakaoController {
    private Logger logger = LoggerFactory.getLogger(KakaoController.class);

    //    @Autowired
    private final KakaoService kakaoService ;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService= kakaoService;
    }
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KakaoClient_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KakaoRedirectUri;

    @RequestMapping("/login/oauth2/code/kakao")
    public String kakaoConnect() {


        String url = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=15a976729f7c3449681112fe55fdfadd" +
                "&redirect_uri=" +
                "http://localhost:3000/login/oauth2/code/kakao" +
                "&response_type=code";
        return "redirect:" + url;
    }

    //이거 호출시급
    @GetMapping(value = "/login/oauth2/callback/kakao")
    public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) throws Exception {
        logger.info("code는 !! : "+code);
        String access_token = kakaoService.getToken(code);//code로 토큰 받음
        logger.info("access_token : " + access_token);


        //토큰으로 사용자 정보 담은 list 가져오기
        ArrayList<Object> list = kakaoService.getUserInfo(access_token);

        //list 모델에 담아 view로 넘김
        model.addAttribute("list", list);

        return "userInfo";
    }

}

