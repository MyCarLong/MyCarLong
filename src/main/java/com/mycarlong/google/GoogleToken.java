package com.mycarlong.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleToken(
        String access_token,
        String expires_in,
        String refresh_token,
        String scope,
        String token_type,
        String id_token
) {
}
