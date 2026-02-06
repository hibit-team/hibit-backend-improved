package com.hibitbackendimproved.post.presentation;

import com.hibitbackendimproved.ControllerTestSupport;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import com.hibitbackendimproved.post.dto.request.PostCreateRequest;
import com.hibitbackendimproved.post.dto.request.PostUpdateRequest;
import com.hibitbackendimproved.post.dto.response.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PostControllerTest extends ControllerTestSupport {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    private static final PostResponse POST_RESPONSE_1 = PostResponse.builder()
            .id(1L)
            .title("게시글 제목1")
            .exhibitionTitle("전시회 제목1")
            .exhibitionPlace("서울시청")
            .exhibitionPrice(10000)
            .exhibitionImage("전시회 이미지1")
            .postStatus(PostStatus.HOLDING)
            .createDateTime(LocalDateTime.now())
            .build();

    private static final PostResponse POST_RESPONSE_2 = PostResponse.builder()
            .id(2L)
            .title("게시글 제목2")
            .exhibitionTitle("전시회 제목2")
            .exhibitionPlace("예술의 전당")
            .exhibitionPrice(20000)
            .exhibitionImage("전시회 이미지2")
            .postStatus(PostStatus.HOLDING)
            .createDateTime(LocalDateTime.now())
            .build();

    @DisplayName("신규 게시글을 등록한다.")
    @Test
    void 신규_게시글을_등록한다() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title(게시글제목)
                .content(게시글내용)
                .exhibitionTitle(전시회제목)
                .exhibitionLink(전시회링크)
                .exhibitionImage(전시회이미지)
                .exhibitionPlace(전시회장소)
                .exhibitionPrice(전시회가격)
                .openChatUrl(오픈채팅방Url)
                .postStatus(모집상태)
                .build();

        given(postService.save(any(), any())).willReturn(프로필_등록_응답());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/posts/new")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("posts/save/success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),

                                        fieldWithPath("exhibitionTitle").type(JsonFieldType.STRING).description("전시회 제목"),
                                        fieldWithPath("exhibitionLink").type(JsonFieldType.STRING).description("전시회 링크"),
                                        fieldWithPath("exhibitionImage").type(JsonFieldType.STRING).description("전시회 이미지"),
                                        fieldWithPath("exhibitionPlace").type(JsonFieldType.STRING).description("전시회 장소"),
                                        fieldWithPath("exhibitionPrice").type(JsonFieldType.NUMBER).description("전시회 가격"),
                                        fieldWithPath("openChatUrl").type(JsonFieldType.STRING).description("오픈 채팅방 URL 주소"),
                                        fieldWithPath("postStatus").type(JsonFieldType.STRING).optional().description("모집상태 타입")),
                                responseFields(
                                        fieldWithPath("meta.code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("meta.message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("data.exhibitionTitle").type(JsonFieldType.STRING).description("전시회 제목"),
                                        fieldWithPath("data.exhibitionLink").type(JsonFieldType.STRING).description("전시회 링크"),
                                        fieldWithPath("data.exhibitionImage").type(JsonFieldType.STRING).description("전시회 이미지"),
                                        fieldWithPath("data.exhibitionPlace").type(JsonFieldType.STRING).description("전시회 장소"),
                                        fieldWithPath("data.exhibitionPrice").type(JsonFieldType.NUMBER).description("전시회 가격"),

                                        fieldWithPath("data.openChatUrl").type(JsonFieldType.STRING).description("오픈 채팅방 URL"),
                                        fieldWithPath("data.postStatus").type(JsonFieldType.STRING).description("모집상태"),
                                        fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("조회수"))
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("등록된 게시글을 모두 조회한다.")
    @Test
    void 등록된_게시글을_모두_조회한다() throws Exception {
        // given
        Member 팬시 = 팬시();
        팬시 = memberRepository.save(팬시);
        List<Post> 게시글_목록 = List.of(프로젝트_해시테크(팬시), 오스틴리_전시회(팬시));
        PostsResponse response = PostsResponse.of(게시글_목록);

        given(postService.findAll()).willReturn(response);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("posts/find/all/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("게시글에 대한 상세 페이지를 조회한다.")
    @Test
    void 게시글에_대한_상세_페이지를_조회한다() throws Exception {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);

        Long postId = 1L;
        PostDetailResponse response = PostDetailResponse.builder()
                .id(postId)
                .writerId(1L)
                .title(게시글제목)
                .content(게시글내용)
                .exhibitionTitle(전시회제목)
                .exhibitionLink(전시회링크)
                .exhibitionImage(전시회이미지)
                .exhibitionPlace(전시회장소)
                .exhibitionPrice(전시회가격)
                .openChatUrl(오픈채팅방Url)
                .postStatus(모집상태)
                .viewCount(0)
                .build();

        // when
        when(postService.findPost(any(), any(), any())).thenReturn(response);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/{id}", postId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("posts/find/one/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("meta.code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("meta.message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("data.exhibitionTitle").type(JsonFieldType.STRING).description("전시회 제목"),
                                fieldWithPath("data.exhibitionLink").type(JsonFieldType.STRING).description("전시회 링크"),
                                fieldWithPath("data.exhibitionImage").type(JsonFieldType.STRING).description("전시회 이미지"),
                                fieldWithPath("data.exhibitionPlace").type(JsonFieldType.STRING).description("전시회 장소"),
                                fieldWithPath("data.exhibitionPrice").type(JsonFieldType.NUMBER).description("전시회 가격"),
                                fieldWithPath("data.openChatUrl").type(JsonFieldType.STRING).description("오픈 채팅방 URL"),
                                fieldWithPath("data.postStatus").type(JsonFieldType.STRING).description("모집상태"),
                                fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("조회수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("검색할 때 특정 제목 또는 내용에 해당하는 값을 입력하면 해당 값이 포함된 개수가 반환된다.")
    @Test
    void searchPostCount() throws Exception {
        // given
        PostsCountResponse countResponse = new PostsCountResponse(5);

        // when
        when(postService.countPostWithQuery(any())).thenReturn(countResponse);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/count?query=제목|내용")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("posts/count/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("특정 게시글 검색시 200을 반환한다.")
    @Test
    void searchSlicePosts() throws Exception {
        // given
        PostsSliceResponse pagePostsResponse = new PostsSliceResponse(
                List.of(POST_RESPONSE_2, POST_RESPONSE_1), true);

        // when
        when(postService.searchSlickWithQuery(any(), any())).thenReturn(pagePostsResponse);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/search?query=제목&size=2&page=0")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("posts/search/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("or 게시글 검색 시 200을 반환한다.")
    @Test
    void searchSlicePosts_or() throws Exception {
        // given
        PostsSliceResponse pagePostsResponse = new PostsSliceResponse(
                List.of(POST_RESPONSE_2, POST_RESPONSE_1), true);

        // when
        when(postService.searchSlickWithQuery(any(), any())).thenReturn(pagePostsResponse);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/search?query=제목2|제목1&size=2&page=0")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("posts/search/success/or",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("and 게시글 검색 시 200을 반환한다.")
    @Test
    void searchSlicePosts_and() throws Exception {
        // given
        PostsSliceResponse pagePostsResponse = new PostsSliceResponse(
                List.of(POST_RESPONSE_2, POST_RESPONSE_1), true);

        // when
        when(postService.searchSlickWithQuery(any(), any())).thenReturn(pagePostsResponse);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/search?query=제목2&제목1&size=2&page=0")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("posts/search/success/and",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("게시글의 일부 속성을 수정하면 204를 반환한다.")
    @Test
    void 게시글의_일부_속성을_수정하면_204를_반환한다() throws Exception {
        // given
        Long postId = 1L;
        willDoNothing()
                .given(postService)
                .update(any(), any(), any());

        PostUpdateRequest request = PostUpdateRequest.builder()
                .title(게시글제목2)
                .content(게시글내용2)
                .exhibitionTitle(전시회제목2)
                .exhibitionLink(전시회링크2)
                .exhibitionImage(전시회이미지2)
                .exhibitionPlace(전시회장소2)
                .exhibitionPrice(전시회가격2)
                .openChatUrl(오픈채팅방Url2)
                .postStatus(모집상태2)
                .build();

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/posts/{postId}", postId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("posts/update/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("본인이 등록한 게시글을 삭제하면 204를 반환한다.")
    @Test
    void 본인이_등록한_게시글을_삭제하면_204를_반환한다() throws Exception {
        // given
        Long postId = 1L;
        willDoNothing()
                .given(postService)
                .update(any(), any(), any());

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/posts/{postId}", postId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("posts/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNoContent());
    }
}
