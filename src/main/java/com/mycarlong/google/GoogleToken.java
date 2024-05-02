package com.mycarlong.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleToken(
        String tokenType,
        String accessToken,
        String idToken,
        Integer expiresIn,
        String scope
) {
}
