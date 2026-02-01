package com.hibitbackendimproved.auth.presentation;

import com.hibitbackendimproved.auth.application.AuthService;
import com.hibitbackendimproved.auth.application.OAuthClient;
import com.hibitbackendimproved.auth.application.OAuthUri;
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
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final OAuthUri oAuthUri;
    private final OAuthClient oAuthClient;
    private final AuthService authService;

    public AuthController(final OAuthUri oAuthUri, final OAuthClient oAuthClient, final AuthService authService) {
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.authService = authService;
    }

    @GetMapping("/{oauthProvider}/oauth-uri")
    public ResponseEntity<ApiResponse<OAuthUriResponse>> generateLink(@PathVariable final String oauthProvider,
                                                         @RequestParam final String redirectUri) {
        OAuthUriResponse oAuthUriResponse = new OAuthUriResponse(oAuthUri.generate(redirectUri));
        ApiResponse<OAuthUriResponse> apiResponse = ApiResponse.ok(oAuthUriResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/{oauthProvider}/token")
    public ResponseEntity<ApiResponse<AccessAndRefreshTokenResponse>> generateAccessAndRefreshToken(
            @PathVariable final String oauthProvider, @Valid @RequestBody final TokenRequest tokenRequest) {
        OAuthMember oAuthMember = oAuthClient.getOAuthMember(tokenRequest.getCode(), tokenRequest.getRedirectUri());
        AccessAndRefreshTokenResponse accessAndRefreshTokenResponse = authService.generateAccessAndRefreshToken(oAuthMember);
        ApiResponse<AccessAndRefreshTokenResponse> apiResponse = ApiResponse.ok(accessAndRefreshTokenResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/token/access")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> generateAccessToken(
           @CookieValue("refreshToken") String refreshToken) {
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest(refreshToken);
        AccessTokenResponse accessTokenResponse = authService.generateAccessToken(tokenRenewalRequest);
        ApiResponse<AccessTokenResponse> apiResponse = ApiResponse.ok(accessTokenResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/validate/token")
    public ResponseEntity<Void> validateToken(@AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal final LoginMember loginMember) {
        authService.deleteToken(loginMember.getId());
        ApiResponse<Void> apiResponse = ApiResponse.noContent();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
}
