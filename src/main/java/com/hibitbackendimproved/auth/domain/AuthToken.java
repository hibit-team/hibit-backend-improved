package com.hibitbackendimproved.auth.domain;

import com.hibitbackendimproved.auth.exception.NotFoundTokenException;
import lombok.Getter;

@Getter
public class AuthToken {

    private final String accessToken;
    private final String refreshToken;

    public AuthToken(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void validateHasSameRefreshToken(final String otherRefreshToken) {
        if (!refreshToken.equals(otherRefreshToken)) {
            throw new NotFoundTokenException("회원의 리프레시 토큰이 아닙니다.");
        }
    }
}
