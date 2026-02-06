package com.hibitbackendimproved.auth.application;


import com.hibitbackendimproved.auth.dto.OAuthMember;

public interface OAuthClient {
    OAuthMember getOAuthMember(final String code, final String redirectUri);

    String getProviderName();
}
