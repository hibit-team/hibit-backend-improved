package com.hibitbackendimproved.profile.presentation;

import com.hibitbackendimproved.ControllerTestSupport;
import com.hibitbackendimproved.profile.dto.request.ProfileCreateRequest;
import com.hibitbackendimproved.profile.dto.request.ProfileUpdateRequest;
import com.hibitbackendimproved.profile.dto.response.ProfileResponse;
import com.hibitbackendimproved.profile.exception.InvalidProfileAlreadyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.타인_프로필_조회_응답;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_닉네임;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_닉네임2;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_이미지;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_이미지2;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_자기소개;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_자기소개2;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_프로필;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends ControllerTestSupport {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @DisplayName("본인 프로필을 등록한다.")
    @Test
    void 본인_프로필을_등록한다() throws Exception {
        // given
        ProfileCreateRequest request = ProfileCreateRequest.builder()
                .nickname(팬시_닉네임)
                .profileImage(팬시_이미지)
                .introduce(팬시_자기소개)
                .build();
        // when & then
        mockMvc.perform(post("/api/v1/profile/new")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("profile/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("profileImage").description("프로필 이미지"),
                                fieldWithPath("introduce").description("자기소개")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @DisplayName("본인의 프로필을 등록할 때 이미 존재하는 닉네임이면 400을 반환한다")
    @Test
    void 본인의_프로필을_등록할_때_이미_존재하는_닉네임이면_400을_반환한다() throws Exception {
        // given
        given(profileService.save(any(), any())).willThrow(new InvalidProfileAlreadyException());

        // when
        mockMvc.perform(post("/api/v1/profile/new")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(팬시_프로필()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("profile/save/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("본인의 프로필을 조회한다.")
    @Test
    void 본인의_프로필을_조회한다() throws Exception {
        // given
        ProfileResponse response = ProfileResponse.builder()
                .nickname(팬시_닉네임)
                .profileImage(팬시_이미지)
                .introduce(팬시_자기소개)
                .build();

        given(profileService.findMyProfile(any())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/profile/me")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("profiles/find/me/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("타인의 프로필을 조회한다")
    @Test
    void 타인의_프로필을_조회한다() throws Exception {
        // given
        Long 타인프로필_id = 2L;
        given(profileService.findOtherProfile(any())).willReturn(타인_프로필_조회_응답());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/profile/other/{id}", 타인프로필_id)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("profile/find/other/one/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("타인 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("본인 프로필을 수정한다.")
    @Test
    void 본인_프로필을_수정한다() throws Exception {
        // given
        ProfileUpdateRequest request = ProfileUpdateRequest.builder()
                .nickname(팬시_닉네임2)
                .profileImage(팬시_이미지2)
                .introduce(팬시_자기소개2)
                .build();

        // when & then
        mockMvc.perform(put("/api/v1/profile/me")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("profile/update/me/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNoContent());
    }
}
