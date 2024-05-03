package com.mycarlong.google;

import static com.mycarlong.mycarlongback.oauth.OauthServerType.GOOGLE;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mycarlong.mycarlongback.oauth.OauthId;
import com.mycarlong.mycarlongback.oauth.OauthMember;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        BigInteger id,
        boolean hasSignedUp,
        LocalDateTime connectedAt,
        GoogleProfile googleProfile
) {

        public OauthMember toDomain() {
                return OauthMember.builder()
                .oauthId(new OauthId(String.valueOf(id), GOOGLE))
                .nickname(googleProfile.name)
                .email(googleProfile.email)
                .build();
        }


@JsonNaming(SnakeCaseStrategy.class)
public record GoogleProfile(
        String id,
        String email,
        String verified_email,
        String name,
        String given_name,
        String family_name,
        String locale,
        String picture
) {
}
}
