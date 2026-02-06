package com.hibitbackendimproved.config.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties("oauth.kakao")
public class KakaoProperties {

    private final String clientId;
    private final String clientSecret;
    private final String authorizationEndpoint;
    private final String responseType;
    private final List<String> scopes;
    private final String tokenUri;
    private final String userInfoUri;

    public KakaoProperties(@Value("${oauth.kakao.client-id}") final String clientId,
                           @Value("${oauth.kakao.client-secret}") final String clientSecret,
                           @Value("${oauth.kakao.authorization-endpoint}") final String authorizationEndpoint,
                           @Value("${oauth.kakao.response-type}") final String responseType,
                           @Value("${oauth.kakao.scopes}") final List<String> scopes,
                           @Value("${oauth.kakao.token-uri}") final String tokenUri,
                           @Value("${oauth.kakao.user-info-uri}") final String userInfoUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationEndpoint = authorizationEndpoint;
        this.responseType = responseType;
        this.scopes = scopes;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }

}
