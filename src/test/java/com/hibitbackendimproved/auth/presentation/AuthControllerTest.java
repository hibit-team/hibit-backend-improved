package com.hibitbackendimproved.auth.presentation;

import com.hibitbackendimproved.ControllerTestSupport;
import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.auth.exception.InvalidTokenException;
import com.hibitbackendimproved.infrastructure.oauth.exception.OAuthException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.hibitbackendimproved.common.AuthFixtures.*;
import static com.hibitbackendimproved.common.fixtures.MemberFixtures.FANCY_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @DisplayName("OAuth 소셜 로그인을 위한 링크와 상태코드 200을 반환한다.")
    @Test
    void OAuth_소셜_로그인을_위한_링크와_상태코드_200을_반환한다() throws Exception {
        // given
        given(oAuthUri.generate(any())).willReturn(OAuth_로그인_링크);

        // when & then
        mockMvc.perform(get("/api/auth/{oauthProvider}/oauth-uri?redirectUri={redirectUri}", GOOGLE_PROVIDER,
                        "https://hibit.com/oauth"))
                .andDo(print())
                .andDo(document("auth/generate/redirectUri/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자 (GOOGLE)")
                        ),
                        queryParameters(
                                parameterWithName("redirectUri").description("OAuth Redirect URI")
                        ),
                        responseFields(
                                fieldWithPath("meta.code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("meta.message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.oAuthUri").type(JsonFieldType.STRING).description("OAuth 소셜 로그인 링크")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 구글 로그인을 하면 accessToken과 refreshToken 값과 상태코드 200을 반환한다.")
    @Test
    void OAuth_구글_로그인을_하면_accessToken과_refreshToken_값과_상태코드_200을_반환한다() throws Exception {
        // given
        given(authService.generateAccessAndRefreshToken(any())).willReturn(MEMBER_인증_코드_토큰_응답());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MEMBER_인증_코드_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/generate/accessTokenAndRefreshToken/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자(GOOGLE)")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("OAuth 로그인 인증 코드"),
                                fieldWithPath("redirectUri").type(JsonFieldType.STRING)
                                        .description("OAuth Redirect URI")
                        ),
                        responseFields(
                                fieldWithPath("meta.code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("meta.message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("히빗 Access Token"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("히빗 Refresh Token"),
                                fieldWithPath("data.isProfileRegistered").type(JsonFieldType.NUMBER).description("프로필 등록 여부")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 로그인 과정에서 Resource Server 에러가 발생하면 상태코드 500을 반환한다.")
    @Test
    void OAuth_로그인_과정에서_Resource_Server_에러가_발생하면_상태코드_500을_반환한다() throws Exception {
        // given
        given(authService.generateAccessAndRefreshToken(any())).willThrow(new OAuthException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MEMBER_인증_코드_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/generate/accessTokenAndRefreshToken/fail/ResourceServiceError",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자(GOOGLE)")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("OAuth 로그인 인증 코드"),
                                fieldWithPath("redirectUri").type(JsonFieldType.STRING)
                                        .description("OAuth Redirect URI")
                        )
                ))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("리프레시 토큰을 통해 새로운 엑세스 토큰을 발급하면 상태코드 200을 반환한다.")
    @Test
    void 리프레시_토큰을_통해_새로운_액세스_토큰을_발급하면_상태코드_200을_반환한다() throws Exception {
        // given
        given(authService.generateAccessToken(any())).willReturn(MEMBER_리뉴얼_토큰_응답());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token/access")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "ccccc.bbbbb.aaaaa")))
                .andDo(print())
                .andDo(document("auth/generate/renewalAccessToken/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("본인의 리프레시 토큰이 아닌 값으로 새로운 액세스 토큰을 발급하면 상태코드 401을 반환한다.")
    @Test
    void 본인의_리프레시_토큰이_아닌_값으로_새로운_액세스_토큰을_발급하면_상태코드_401을_반환한다() throws Exception {
        // given
        given(authService.generateAccessToken(any())).willThrow(new InvalidTokenException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token/access")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "ccccc.bbbbb.aaaaa")))
                .andDo(print())
                .andDo(document("auth/generate/renewalAccessToken/fail/isUnauthorized",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그아웃을 정상적으로 하면 204을 반환한다.")
    @Test
    void 로그아웃을_정상적으로_하면_204을_반환한다() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(FANCY_ID);
        willDoNothing().given(authService).deleteToken(loginMember.getId());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/auth/logout")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "ccccc.bbbbb.aaaaa")))
                .andDo(print())
                .andDo(document("auth/logout/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNoContent());
    }
}
