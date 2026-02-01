package com.hibitbackendimproved.post.domain;

import com.hibitbackendimproved.IntegrationTestSupport;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.post.exception.NotFoundPostException;
import com.hibitbackendimproved.profile.domain.Profile;
import com.hibitbackendimproved.profile.domain.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.*;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_프로필;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ActiveProfiles("test")
@Transactional
class PostRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager em;

    private Member member;

    private Profile profile;

    private Post post0;
    private Post post1;
    private Post post2;

    @BeforeEach
    void setUp() {
        member = 팬시();
        memberRepository.save(member);
        profile = 팬시_프로필(member);
        profileRepository.save(profile);
        post0 = 오스틴리_전시회(member);
        post1 = 프로젝트_해시테크(member);
        post2 = 프로젝트_해시테크_2(member);
        postRepository.saveAll(List.of(post0, post1, post2));
    }

    @DisplayName("게시글과 회원 테이블이 정상적으로 매핑이 된다.")
    @Test
    void 게시글과_회원_테이블이_정상적으로_매핑이_된다() {
        // given
        Post foundPost = postRepository.findById(post1.getId())
                .orElseThrow(NotFoundPostException::new);

        // when & then
        Assertions.assertThat(foundPost.getMember().getId()).isNotNull();
    }

    @DisplayName("신규로 등록된 게시글을 최신순으로 모두 가져온다.")
    @Test
    void findAllByOrderByCreatedDateTimeDesc() {
        // given
        List<Post> posts = postRepository.findAllByOrderByCreatedDateTimeDesc();

        // when & then
        assertThat(posts)
                .extracting(Post::getTitle, Post::getContent)
                .containsExactly(
                        tuple(posts.get(0).getTitle(), posts.get(0).getContent()),
                        tuple(posts.get(1).getTitle(), posts.get(1).getContent()),
                        tuple(posts.get(2).getTitle(), posts.get(2).getContent())
                );
    }

    @DisplayName("특정 게시글의 viewCount 를 1 증가시킨다.")
    @Test
    void updateViewCount() {
        // given
        int initViewCount = postRepository.findById(post1.getId()).get().getViewCount();
        postRepository.updateViewCount(post1.getId());
        em.clear();
        int viewCount = postRepository.findById(post1.getId()).get().getViewCount();

        // when & then
        assertThat(initViewCount + 1).isEqualTo(viewCount);
    }

    @DisplayName("특정 쿼리에 부합하는 글의 개수를 가져온다.")
    @Test
    void findPostPagesByQuery() {
        // given
        Page<Post> result = postRepository.findPostPagesByQuery(PageRequest.of(0, 3, DESC, "created_date_time"), "");

        // when & then
        assertThat(result.getTotalElements()).isEqualTo(3L);
    }

    @DisplayName("특정 쿼리에 부합하는 게시글을 최신순으로 가져온다.")
    @Test
    void findPostSlicePageByQuery() {
        // given
        Slice<Post> result = postRepository.findPostSlicePageByQuery(PageRequest.of(0, 2, DESC, "created_date_time"), "");

        // when & then
        assertThat(result.getContent()).containsExactly(post2, post1);
        assertThat(result.isLast()).isEqualTo(false);
    }
}
