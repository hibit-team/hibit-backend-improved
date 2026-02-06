package com.hibitbackendimproved.auth.dto.response;

import lombok.Getter;

@Getter
public class AccessAndRefreshTokenResponse {

    private final String accessToken;

    private final String refreshToken;


    public AccessAndRefreshTokenResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
