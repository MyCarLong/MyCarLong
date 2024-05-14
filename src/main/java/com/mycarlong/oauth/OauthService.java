package com.mycarlong.oauth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;


    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public Long login(OauthServerType oauthServerType, String authCode) {
      OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
      OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
              .orElseGet(() -> oauthMemberRepository.save(oauthMember));
      return saved.id();
  }

}
