package com.hibitbackendimproved.infrastructure.oauth.uri;

import com.hibitbackendimproved.auth.application.OAuthUri;
import com.hibitbackendimproved.config.oauth.KakaoProperties;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuthUri implements OAuthUri {

    private static final String KAKAO = "kakao";
    private final KakaoProperties properties;

    public KakaoOAuthUri(final KakaoProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate(final String redirectUri) {
        return properties.getAuthorizationEndpoint() + "?"
                + "response_type=" + properties.getResponseType() + "&"
                + "client_id=" + properties.getClientId() + "&"
                + "redirect_uri=" + redirectUri + "&"
                + "scope=" + String.join(" ", properties.getScopes());

    }

    @Override
    public String getProviderName() {
        return KAKAO;
    }
}
