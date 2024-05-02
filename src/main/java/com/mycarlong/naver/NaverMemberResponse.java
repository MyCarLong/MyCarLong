package com.mycarlong.naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mycarlong.kakao.OauthId;
import com.mycarlong.kakao.OauthMember;

import static com.mycarlong.kakao.OauthServerType.NAVER;

@JsonNaming(value = SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {

  public OauthMember toDomain() {
    return OauthMember.builder()
            .oauthId(new OauthId(String.valueOf(response.id), NAVER))
            .nickname(response.nickname)
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
