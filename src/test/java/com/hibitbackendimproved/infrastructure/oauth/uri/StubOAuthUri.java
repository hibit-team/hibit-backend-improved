package com.hibitbackendimproved.infrastructure.oauth.uri;


import com.hibitbackendimproved.auth.application.OAuthUri;

public class StubOAuthUri implements OAuthUri {

    @Override
    public String generate(final String redirectUri) {
        return "https://localhost:3000";
    }

    @Override
    public String getProviderName() {
        return "oauth";
    }
}
