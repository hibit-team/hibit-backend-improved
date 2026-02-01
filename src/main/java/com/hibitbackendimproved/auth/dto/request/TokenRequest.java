package com.hibitbackendimproved.auth.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TokenRequest {

    @NotBlank(message = "인가 코드는 공백일 수 없습니다.")
    private String code;

    @NotNull(message = "Null일 수 없습니다.")
    private String redirectUri;

    private TokenRequest() {
    }

    public TokenRequest(final String code, final String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
