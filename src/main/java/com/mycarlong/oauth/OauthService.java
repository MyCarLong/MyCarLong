package com.mycarlong.oauth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthService implements UserDetailsService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    // 특정 OAuth 서버 타입에 대한 인증 코드 요청 URL을 반환
    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public Map<String, Object> login(OauthServerType oauthServerType, String authCode) {
        // 인증 코드를 사용하여 OAuth 멤버 정보를 가져옴
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        // 저장된 OAuth 멤버 정보를 가져오거나 새로 저장
        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
        // 로그인 정보와 닉네임을 반환
        return Map.of("id", saved.id(), "nickname", oauthMember.nickname());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
