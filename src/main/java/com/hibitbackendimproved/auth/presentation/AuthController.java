package com.hibitbackendimproved.auth.presentation;

import com.hibitbackendimproved.auth.application.AuthService;
import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.auth.dto.OAuthMember;
import com.hibitbackendimproved.auth.dto.request.TokenRenewalRequest;
import com.hibitbackendimproved.auth.dto.request.TokenRequest;
import com.hibitbackendimproved.auth.dto.response.AccessAndRefreshTokenResponse;
import com.hibitbackendimproved.auth.dto.response.AccessTokenResponse;
import com.hibitbackendimproved.auth.dto.response.OAuthUriResponse;
import com.hibitbackendimproved.support.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/v1/auth/{oauthProvider}/oauth-uri")
    public ResponseEntity<ApiResponse<OAuthUriResponse>> generateLink(@PathVariable final String oauthProvider,
                                                                      @RequestParam final String redirectUri) {
        String oauthUri = authService.generateOAuthUri(oauthProvider, redirectUri);
        OAuthUriResponse oAuthUriResponse = new OAuthUriResponse(oauthUri);
        ApiResponse<OAuthUriResponse> apiResponse = ApiResponse.ok(oAuthUriResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/v1/auth/{oauthProvider}/token")
    public ResponseEntity<ApiResponse<AccessAndRefreshTokenResponse>> generateAccessAndRefreshToken(
            @PathVariable final String oauthProvider,
            @Valid @RequestBody final TokenRequest tokenRequest) {
        OAuthMember oAuthMember = authService.handleOAuth(oauthProvider, tokenRequest.getCode(), tokenRequest.getRedirectUri());
        AccessAndRefreshTokenResponse accessAndRefreshTokenResponse = authService.generateAccessAndRefreshToken(oAuthMember);
        ApiResponse<AccessAndRefreshTokenResponse> apiResponse = ApiResponse.ok(accessAndRefreshTokenResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/v1/auth/token/access")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> generateAccessToken(
            @CookieValue("refreshToken") final String refreshToken) {
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest(refreshToken);
        AccessTokenResponse accessTokenResponse = authService.generateAccessToken(tokenRenewalRequest);
        ApiResponse<AccessTokenResponse> apiResponse = ApiResponse.ok(accessTokenResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/v1/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal final LoginMember loginMember) {
        authService.deleteToken(loginMember.getId());
        ApiResponse<Void> apiResponse = ApiResponse.noContent();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
}
