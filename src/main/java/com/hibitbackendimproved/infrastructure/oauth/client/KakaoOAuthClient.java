package com.hibitbackendimproved.infrastructure.oauth.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibitbackendimproved.auth.application.OAuthClient;
import com.hibitbackendimproved.auth.dto.OAuthMember;
import com.hibitbackendimproved.auth.exception.ServerErrorOAuthException;
import com.hibitbackendimproved.config.oauth.KakaoProperties;
import com.hibitbackendimproved.infrastructure.oauth.dto.KakaoTokenResponse;
import com.hibitbackendimproved.infrastructure.oauth.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoOAuthClient implements OAuthClient {

    private static final String KAKAO = "kakao";
    private final KakaoProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoOAuthClient(final KakaoProperties kakaoProperties,
                            final RestTemplateBuilder restTemplateBuilder,
                            final ObjectMapper objectMapper) {
        this.properties = kakaoProperties;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public OAuthMember getOAuthMember(final String code, final String redirectUri) {
        KakaoTokenResponse kakaoTokenResponse = requestKakaoToken(code, redirectUri);
        KakaoUserInfo kakaoUserInfo = requestUserInfo(kakaoTokenResponse.getAccessToken());

        String refreshToken = kakaoTokenResponse.getAccessToken();
        return new OAuthMember(kakaoUserInfo.getNickname(), kakaoUserInfo.getProfileImage(), refreshToken);
    }

    private KakaoTokenResponse requestKakaoToken(final String code, final String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = generateTokenParams(code, redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return fetchKakaoToken(request).getBody();

    }

    private MultiValueMap<String, String> generateTokenParams(final String code, final String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", properties.getClientId());
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", properties.getClientSecret());
        return params;
    }

    private ResponseEntity<KakaoTokenResponse> fetchKakaoToken(final HttpEntity<MultiValueMap<String, String>> request) {
        try {
            return restTemplate.postForEntity(properties.getTokenUri(), request, KakaoTokenResponse.class);
        } catch (final RestClientException e) {
            throw new ServerErrorOAuthException();
        }
    }

    private KakaoUserInfo requestUserInfo(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(properties.getUserInfoUri(), HttpMethod.GET, request, String.class);
            // ObjectMapper를 사용하여 JSON 응답을 KakaoUserInfo 객체로 변환
            return objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
        } catch (RestClientException | JsonProcessingException e) {
            throw new ServerErrorOAuthException("Failed to fetch user info");
        }
    }

    @Override
    public String getProviderName() {
        return KAKAO;
    }
}
