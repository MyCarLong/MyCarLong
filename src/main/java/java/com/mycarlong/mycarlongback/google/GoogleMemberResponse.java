package java.com.mycarlong.mycarlongback.google;

import static com.mycarlong.mycarlongback.oauth.OauthServerType.GOOGLE;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mycarlong.mycarlongback.oauth.OauthId;
import com.mycarlong.mycarlongback.oauth.OauthMember;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        Long id,
        boolean hasSignedUp,
        LocalDateTime connectedAt,
        GoogleAccount googleAccount
) {

        public OauthMember toDomain() {
                return OauthMember.builder()
                        .oauthId(new OauthId(String.valueOf(id), GOOGLE))
                        .nickname(googleAccount.profile.name)
                        .email(googleAccount.profile.email)
                        .build();
        }
        @JsonNaming(SnakeCaseStrategy.class)
        public record GoogleAccount(
                Profile profile,
                String name,
                String email
) {
}

@JsonNaming(SnakeCaseStrategy.class)
public record Profile(
        String name,
        String email
) {
}
}
