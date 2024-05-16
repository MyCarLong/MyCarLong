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


    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public Map<String,Object> login(OauthServerType oauthServerType, String authCode) {
      OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
      OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
              .orElseGet(() -> oauthMemberRepository.save(oauthMember));

      return Map.of("id",saved.id(), "nickname",oauthMember.nickname());
  }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
