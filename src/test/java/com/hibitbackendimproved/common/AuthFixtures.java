package com.hibitbackendimproved.common;

import com.hibitbackendimproved.auth.dto.request.TokenRenewalRequest;
import com.hibitbackendimproved.auth.dto.request.TokenRequest;
import com.hibitbackendimproved.auth.dto.response.AccessAndRefreshTokenResponse;
import com.hibitbackendimproved.auth.dto.response.AccessTokenResponse;

public class AuthFixtures {

    public static final String KAKAO_PROVIDER = "kakao";
    public static final String OAUTH_PROVIDER = "oauthProvider";

    public static final String STUB_MEMBER_인증_코드 = "member authorization code";
    public static final String STUB_MEMBER_REFRESH_인증_코드 = "member refresh authorization code";

    public static final String 더미_엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 더미_리프레시_토큰 = "ccccc.bbbbb.aaaaa";
    public static final String OAuth_로그인_링크 = "https://kauth.kakao.com/oauth/authorize?client_id=test_client_id&redirect_uri=https://hibit.site/oauth&response_type=code";

    public static final String 더미_시크릿_키 = "fancykijuhnbmsowishcxbzcsdjsajdabwcksjadaksdhabdsadasjkdb";

    public static final String STUB_OAUTH_ACCESS_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";

    public static TokenRequest MEMBER_인증_코드_토큰_요청() {
        return new TokenRequest(STUB_MEMBER_인증_코드, "https://hibit.site/oauth");
    }

    public static AccessAndRefreshTokenResponse MEMBER_인증_코드_토큰_응답() {
        return new AccessAndRefreshTokenResponse(STUB_MEMBER_인증_코드, STUB_MEMBER_REFRESH_인증_코드);
    }

    public static TokenRenewalRequest MEMBER_리뉴얼_토큰_요청() {
        return new TokenRenewalRequest(더미_리프레시_토큰);
    }

    public static AccessTokenResponse MEMBER_리뉴얼_토큰_응답() {
        return new AccessTokenResponse(더미_엑세스_토큰);
    }
}
