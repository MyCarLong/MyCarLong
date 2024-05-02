package com.mycarlong.service;


import com.mycarlong.dto.*;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // OAuth2UserRequest로부터 사용자 정보를 로드합니다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 기본 OAuth2UserService의 loadUser 메서드를 호출하여 OAuth2 사용자 정보를 가져옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.info("this oAuth2User name {}",oAuth2User.getName());
        logger.info("this user getAttributes :{}",oAuth2User.getAttributes());
        logger.info("this user getAttributes.get(nickname) :{}",oAuth2User.getAttributes().get("nickname"));

        // OAuth2User에서 공급자 등록 ID를 가져옵니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        String nickname = null;
        // 공급자 등록 ID에 따라 사용자 응답 객체를 생성합니다.
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
//            nickname = (String) ((Map) oAuth2User.getAttributes().get("properties")).get("nickname");
//            logger.info("this user .getAttributes().get(\"properties\")).get(\"nickname\") :{}", nickname);

        } else {
            // 지원되지 않는 공급자인 경우 null을 반환합니다.
            return null;
        }

        // 사용자 이름을 공급자 및 공급자 ID로 설정합니다.
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        logger.info("Oauth2Response_UserName :{}",oAuth2Response.getName());
        logger.info("username {}", username);

        // 사용자가 데이터베이스에 이미 존재하는지 확인합니다.
        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {
            // 데이터베이스에 사용자가 없으면 새로운 사용자 엔티티를 만들고 저장합니다.
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            if (registrationId.equals("kakao")){
                userEntity.setName((String) ((Map) oAuth2User.getAttributes().get("properties")).get("nickname"));
            }
            userEntity.setRole("ROLE_USER");
            userRepository.save(userEntity);

            // 새로운 사용자의 DTO를 생성하고 반환합니다.
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            if (registrationId.equals("kakao")){
                userDTO.setName((String) ((Map) oAuth2User.getAttributes().get("properties")).get("nickname"));
            }
            userDTO.setRole("ROLE_USER");
            return new CustomOAuth2User(userDTO);
        } else {
            // 이미 존재하는 사용자인 경우 사용자 정보를 업데이트하고 저장합니다.
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            if (registrationId.equals("kakao")){
                existData.setName((String) ((Map) oAuth2User.getAttributes().get("properties")).get("nickname"));
            }
            userRepository.save(existData);

            // 업데이트된 사용자의 DTO를 생성하고 반환합니다.
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            if (registrationId.equals("kakao")){
                userDTO.setName((String) ((Map) oAuth2User.getAttributes().get("properties")).get("nickname"));
            }
            userDTO.setRole(existData.getRole());
            return new CustomOAuth2User(userDTO);
        }
    }
}
