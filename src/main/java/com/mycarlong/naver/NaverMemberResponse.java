package com.mycarlong.naver;

import static com.mycarlong.mycarlongback.oauth.OauthServerType.NAVER;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mycarlong.mycarlongback.oauth.OauthId;
import com.mycarlong.mycarlongback.oauth.OauthMember;

@JsonNaming(value = SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {

  public OauthMember toDomain() {
    return OauthMember.builder()
            .oauthId(new OauthId(String.valueOf(response.id), NAVER))
            .nickname(response.name)
            .email(response.email)
            .build();
}

    @JsonNaming(value = SnakeCaseStrategy.class)
    public record Response(
            String id,
            String nickname,
            String name,
            String email,
            String gender,
            String age,
            String birthday,
            String profileImage,
            String birthyear,
            String mobile
    ) {
    }
}
