package com.hibitbackendimproved.infrastructure.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String expiresIn;
    private String tokenType;
    private String scope;

    private KakaoTokenResponse() {
    }

    public KakaoTokenResponse(final String accessToken,
                              final String refreshToken,
                              final String idToken,
                              final String expiresIn,
                              final String tokenType,
                              final String scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.idToken = idToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.scope = scope;
    }
}
