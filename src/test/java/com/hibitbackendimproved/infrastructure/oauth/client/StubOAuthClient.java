package com.hibitbackendimproved.infrastructure.oauth.client;


import com.hibitbackendimproved.auth.application.OAuthClient;
import com.hibitbackendimproved.auth.dto.OAuthMember;
import com.hibitbackendimproved.auth.dto.response.OAuthAccessTokenResponse;
import com.hibitbackendimproved.common.OAuthFixtures;

import static com.hibitbackendimproved.common.AuthFixtures.STUB_OAUTH_ACCESS_TOKEN;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code, final String redirectUri) {
        return OAuthFixtures.parseOAuthMember(code);
    }

    @Override
    public String getProviderName() {
        return "";
    }
}
