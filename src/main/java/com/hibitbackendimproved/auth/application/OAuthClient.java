package com.hibitbackendimproved.auth.application;


import com.hibitbackendimproved.auth.dto.OAuthMember;
import com.hibitbackendimproved.auth.dto.response.OAuthAccessTokenResponse;

public interface OAuthClient {
    OAuthMember getOAuthMember(final String code, final String redirectUri);

    String getProviderName();
}
