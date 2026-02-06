package com.hibitbackendimproved.post.application;

import com.hibitbackendimproved.IntegrationTestSupport;
import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostRepository;
import com.hibitbackendimproved.post.dto.request.PostCreateRequest;
import com.hibitbackendimproved.post.dto.request.PostUpdateServiceRequest;
import com.hibitbackendimproved.post.dto.response.PostDetailResponse;
import com.hibitbackendimproved.post.dto.response.PostResponse;
import com.hibitbackendimproved.post.dto.response.PostsCountResponse;
import com.hibitbackendimproved.post.dto.response.PostsSliceResponse;
import com.hibitbackendimproved.post.exception.NotFoundPostException;
import com.hibitbackendimproved.profile.domain.Profile;
import com.hibitbackendimproved.profile.domain.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.*;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_프로필;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Direction.DESC;

class PostServiceTest extends IntegrationTestSupport {

    private static final String EMPTY_COOKIE_VALUE = "";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService;

    @PersistenceContext
    private EntityManager em;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        profileRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 등록한 페이지를 반환한다.")
    @Test
    void 게시글을_등록한_페이지를_반환한다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        profileRepository.save(팬시_프로필);

        PostCreateRequest request = getPostCreateRequest();

        // when
        LoginMember loginMember = new LoginMember(팬시.getId());
        PostDetailResponse response = postService.save(loginMember, request);
        Post actual = postRepository.findById(response.getId()).orElseThrow();

        // then
        assertThat(response.getTitle()).isNotNull();
        assertAll(
                () -> assertEquals(request.getTitle(), actual.getTitle().getValue()),
                () -> assertEquals(request.getContent(), actual.getContent().getValue()),
                () -> assertEquals(request.getExhibition(), actual.getExhibition().getValue()),
                () -> assertEquals(request.getExhibitionImage(), actual.getExhibitionImage()),
                () -> assertEquals(request.getOpenChatUrl(), actual.getOpenChatUrl()),
                () -> assertEquals(request.getPostStatus(), actual.getPostStatus())
        );
    }

    @DisplayName("게시글에 대한 상세 페이지를 반환한다.")
    @Test
    void 게시글에_대한_상세_페이지를_반환한다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        Profile profile = profileRepository.save(팬시_프로필);
        memberRepository.save(팬시);

        Post post = 프로젝트_해시테크(profile.getMember());
        Long savedPostId = postRepository.save(post).getId();

        // when
        PostDetailResponse response = postService.findPost(savedPostId, LOGIN_MEMBER, EMPTY_COOKIE_VALUE);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(post.getId()),
                () -> assertThat(response.getWriterId()).isEqualTo(post.getMember().getId()),
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle().getValue()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent().getValue()),
                () -> assertThat(response.getExhibition()).isEqualTo(post.getExhibition().getValue())
        );
    }

    @DisplayName("특정 게시글을 조회하면 조회수를 1 증가시킨다.")
    @Test
    void 특정_게시글을_조회하면_조회수를_1_증가시킨다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        Profile profile = profileRepository.save(팬시_프로필);
        memberRepository.save(팬시);

        Post post = 프로젝트_해시테크(profile.getMember());
        postRepository.save(post);

        // when
        int viewCount = post.getViewCount();
        postService.findPost(post.getId(), LOGIN_MEMBER, EMPTY_COOKIE_VALUE);
        em.clear();

        int updatedViewCount = postRepository.findById(post.getId()).get().getViewCount();

        // then
        assertThat(viewCount + 1).isEqualTo(updatedViewCount);
    }

    @DisplayName("오늘 처음 방문한 게시글을 조회하면 조회수가 1 증가되고, 이미 방문한 게시글을 조회하면 조회수가 증가하지 않는다.")
    @ParameterizedTest
    @MethodSource("argsOfFindPostViewCount")
    void 오늘_처음_방문한_게시글을_조회하면_조회수가_1_증가되고_이미_방문한_게시글을_조회하면_조회수가_증가하지_않는다(String logs, int expectedIncreasedViewCount) {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        Profile profile = profileRepository.save(팬시_프로필);
        memberRepository.save(팬시);

        Post 게시글 = 프로젝트_해시테크(profile.getMember());
        postRepository.save(게시글);

        int viewCount = 게시글.getViewCount();
        postService.findPost(게시글.getId(), LOGIN_MEMBER, logs);
        em.clear();
        // when
        int updatedViewCount = postRepository.findById(게시글.getId())
                .get()
                .getViewCount();

        // then
        assertThat(viewCount + expectedIncreasedViewCount).isEqualTo(updatedViewCount);
    }

    private static Stream<Arguments> argsOfFindPostViewCount() {
        int today = LocalDateTime.now().getDayOfMonth();
        return Stream.of(
                Arguments.of(EMPTY_COOKIE_VALUE, 1),
                Arguments.of(today + ":2/3", 1)
        );
    }

    @DisplayName("주어진 쿼리로 정확히 한 개의 게시글을 검색할 수 있다.")
    @Test
    void 주어진_쿼리로_정확히_한_개의_게시글을_검색할_수_있다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        Profile profile = profileRepository.save(팬시_프로필);
        memberRepository.save(팬시);

        Post 게시글_1 = 프로젝트_해시테크(profile.getMember());
        Post 게시글_2 = 오스틴리_전시회(profile.getMember());
        postRepository.saveAll(List.of(게시글_1, 게시글_2));

        String query = "오스틴리";

        // when
        PostsSliceResponse myPosts = postService.searchSlickWithQuery(query,
                PageRequest.of(0, 3, DESC, "created_at"));
        PostsCountResponse response = postService.countPostWithQuery(query);

        // then
        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostResponse.from(게시글_2))),
                () -> assertThat(response.getTotalPostCount()).isEqualTo(1)
        );
    }

    @DisplayName("주어진 쿼리로 제목과 본문 안에서 검색이 가능하다.")
    @Test
    void 주어진_쿼리로_제목과_본문_안에서_검색이_가능하다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        profileRepository.save(팬시_프로필);

        Post post1 = 프로젝트_해시테크(member);
        Post post2 = 오스틴리_전시회(member);
        postRepository.saveAll(List.of(post1, post2));

        String query = "프로젝트";

        // when
        PostsSliceResponse myPosts = postService.searchSlickWithQuery(query,
                PageRequest.of(0, 2, DESC, "created_at"));
        PostsCountResponse response = postService.countPostWithQuery(query);

        // then
        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostResponse.from(post1))),
                () -> assertThat(response.getTotalPostCount()).isEqualTo(1)
        );
    }

    @DisplayName("인젝션 위험 쿼리는 빈 쿼리로 대체한다. (lastPage는 true로 반환된다)")
    @ParameterizedTest
    @CsvSource(value = {"select", "insert", "update", "delete", "()"})
    void 인젝션_위험_쿼리는_빈_쿼리로_대체한다(String query) {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        Profile profile = profileRepository.save(팬시_프로필);
        memberRepository.save(팬시);

        Post post1 = 프로젝트_해시테크(profile.getMember());
        Post post2 = 오스틴리_전시회(profile.getMember());
        postRepository.saveAll(List.of(post1, post2));

        // when
        PostsSliceResponse myPosts = postService.searchSlickWithQuery(query,
                PageRequest.of(0, 2, DESC, "created_at"));
        PostsCountResponse response = postService.countPostWithQuery(query);

        // then
        assertAll(
                () -> assertThat(myPosts.isLastPage()).isEqualTo(true),
                () -> assertThat(response.getTotalPostCount()).isEqualTo(2)
        );
    }

    @DisplayName("본인이 등록한 게시글 정보에서 일부 혹은 전체 속성을 수정할 수 있다.")
    @Test
    void 본인이_등록한_게시글_정보에서_일부_혹은_전체_속성을_수정할_수_있다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);

        Post post = 프로젝트_해시테크(팬시);
        Long savedPostId = postRepository.save(post).getId();
        PostUpdateServiceRequest request = getPostUpdateServiceRequest();

        // when
        postService.update(팬시.getId(), savedPostId, request);

        // then
        Post updatedPost = postRepository.findById(savedPostId).orElseThrow(() -> new NotFoundPostException());
        assertAll(
                () -> assertThat(updatedPost.getTitle().getValue()).isEqualTo(request.getTitle()),
                () -> assertThat(updatedPost.getContent().getValue()).isEqualTo(request.getContent()),
                () -> assertThat(updatedPost.getExhibition().getValue()).isEqualTo(request.getExhibition()),
                () -> assertThat(updatedPost.getExhibitionImage()).isEqualTo(request.getExhibitionImage()),
                () -> assertThat(updatedPost.getOpenChatUrl()).isEqualTo(request.getOpenChatUrl()),
                () -> assertThat(updatedPost.getPostStatus()).isEqualTo(request.getPostStatus())
        );
    }

    @DisplayName("본인이 등록한 게시글을 삭제할 수 있다.")
    @Test
    void 본인이_등록한_게시글을_삭제할_수_있다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile 팬시_프로필 = 팬시_프로필(member);
        Profile profile = profileRepository.save(팬시_프로필);
        memberRepository.save(팬시);

        Post post = 프로젝트_해시테크(profile.getMember());
        Long savedPostId = postRepository.save(post).getId();

        // when
        postService.delete(팬시.getId(), savedPostId);

        // then
        Optional<Post> foundPost = postRepository.findById(savedPostId);
        assertThat(foundPost).isEmpty();
    }

    private static PostCreateRequest getPostCreateRequest() {
        PostCreateRequest request = PostCreateRequest.builder()
                .title(게시글제목)
                .content(게시글내용)
                .exhibition(전시회제목)
                .exhibitionImage(전시회이미지)
                .openChatUrl(오픈채팅방Url)
                .postStatus(모집상태)
                .build();
        return request;
    }

    private static PostUpdateServiceRequest getPostUpdateServiceRequest() {
        PostUpdateServiceRequest request = PostUpdateServiceRequest.builder()
                .title(게시글제목2)
                .content(게시글내용2)
                .exhibition(전시회제목2)
                .exhibitionImage(전시회이미지2)
                .openChatUrl(오픈채팅방Url2)
                .postStatus(모집상태2)
                .build();
        return request;
    }
}
