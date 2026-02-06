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
import static com.hibitbackendimproved.common.fixtures.PostFixtures.오스틴리_전시회;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.프로젝트_해시테크;
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

    @BeforeEach
    void setUp() {
        member = 팬시();
        memberRepository.save(member);
        Profile profile = 팬시_프로필(member);
        profileRepository.save(profile);
    }

    @DisplayName("게시글과 회원 테이블이 정상적으로 매핑이 된다.")
    @Test
    void 게시글과_회원_테이블이_정상적으로_매핑이_된다() {
        // given
        final Post post = createPost(프로젝트_해시테크(member));

        // when
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(NotFoundPostException::new);

        // when & then
        Assertions.assertThat(foundPost.getMember().getId()).isNotNull();
    }

    @DisplayName("신규로 등록된 게시글을 최신순으로 모두 가져온다.")
    @Test
    void findAllByOrderByCreatedDateTimeDesc() {
        // given
        final Post post1 = createPost(프로젝트_해시테크(member));
        final Post post2 = createPost(오스틴리_전시회(member));

        // when
        List<Post> posts = postRepository.findAllByOrderByCreatedDateTimeDesc();

        //  then
        assertThat(posts).hasSize(2)
                .extracting(p -> p.getTitle().getValue(), p -> p.getContent().getValue())
                .containsExactly(
                        tuple(post2.getTitle().getValue(), post2.getContent().getValue()),
                        tuple(post1.getTitle().getValue(), post1.getContent().getValue())
                );
    }

    @DisplayName("특정 게시글의 viewCount 를 1 증가시킨다.")
    @Test
    void updateViewCount() {
        // given
        Post post = createPost(프로젝트_해시테크(member));
        int initViewCount = post.getViewCount();

        // when
        postRepository.updateViewCount(post.getId());
        em.clear();

        // when & then
        int viewCount = postRepository.findById(post.getId()).get().getViewCount();
        assertThat(initViewCount + 1).isEqualTo(viewCount);
    }

    @DisplayName("특정 쿼리에 부합하는 글의 개수를 가져온다.")
    @Test
    void findPostPagesByQuery() {
        // given
        createPost(프로젝트_해시테크(member));
        createPost(오스틴리_전시회(member));

        // when
        Page<Post> result = postRepository.findPostPagesByQuery(PageRequest.of(0, 2, DESC, "created_at"), "");

        // when & then
        assertThat(result.getTotalElements()).isEqualTo(2L);
    }

    @DisplayName("특정 쿼리에 부합하는 게시글을 최신순으로 가져온다.")
    @Test
    void findPostSlicePageByQuery() {
        // given
        Post post1 = createPost(프로젝트_해시테크(member));
        Post post2 = createPost(오스틴리_전시회(member));
        Post post3 = createPost(프로젝트_해시테크(member));

        // when
        Slice<Post> result = postRepository.findPostSlicePageByQuery(PageRequest.of(0, 2, DESC, "created_at"), "");

        // when & then
        assertThat(result.getContent())
                .extracting(Post::getId)
                .containsExactly(post3.getId(), post2.getId());
        assertThat(result.hasNext()).isTrue(); // 다음 페이지 존재함(true)
        assertThat(result.isLast()).isEqualTo(false); // 다음 페이지가 있으므로 마지막 페이지가 아님(false)
    }

    private Post createPost(final Post post) {
        return postRepository.save(post);
    }
}
