package com.mycarlong.naver;

import static com.mycarlong.oauth.OauthServerType.NAVER;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mycarlong.oauth.OauthId;
import com.mycarlong.oauth.OauthMember;

@JsonNaming(value = SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {
    // 받아온 데이터로 회원정보 생성
  public OauthMember toDomain() {
    return OauthMember.builder()
            .oauthId(new OauthId(String.valueOf(response.id), NAVER))
            .nickname(response.name)
            .email(response.email)
            .role("ROLE_USER")
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
