package com.hibitbackendrefactor.post.domain;

import com.hibitbackendrefactor.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.hibitbackendrefactor.common.fixtures.MemberFixtures.*;
import static com.hibitbackendrefactor.common.fixtures.PostFixtures.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class PostRepositoryTest {


    @Autowired
    private PostRepository postRepository;

    @DisplayName("등록된 모든 게시글을 조회한다.")
    @Test
    void findAllByPosts() {
        // given
        Post post1 = createPost(팬시(), 게시글제목, 게시글내용, 전시회제목, 전시관람인원, 전시관람희망날짜, 오픈채팅방Url, 함께하고싶은활동, 게시글이미지들, 모집상태);
        Post post2 = createPost(브루스(), 게시글제목2, 게시글내용2, 전시회제목2, 전시관람인원2, 전시관람희망날짜2, 오픈채팅방Url2, 함께하고싶은활동2, 게시글이미지들2, 모집상태2);

        postRepository.saveAll(List.of(post1, post2));

        // when
        Pageable pageable = PageRequest.of(0, 3);
        List<Post> posts = postRepository.findAllByOrderByCreatedDateTimeDesc(pageable);

        // then
        Assertions.assertThat(posts).hasSize(2)
                .extracting("title", "content", "openChatUrl")
                .containsExactlyInAnyOrder(
                        tuple("프로젝트_해시테크", "프로젝트 해시 태크(http://projecthashtag.net/) 보러가실 분 있으면 아래 댓글 남겨주세요~", "http://projecthashtag.net/"),
                        tuple("오스틴리 전시회", "오스틴리 전시회 보고, 같이 담소하게 얘기 나누실 분 있으시면 아래 댓글 남겨주세요~", "http://ostin.net/")
                );
     }


     private Post createPost(Member member, String title, String content,
                             String exhibition, int exhibitionAttendance, List<PostPossibleTime> postPossibleTimes,
                             String openChatUrl, TogetherActivity togetherActivity, List<PostImage> postImages, PostStatus postStatus) {

        Post post1 = Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .exhibition(exhibition)
                .exhibitionAttendance(exhibitionAttendance)
                .possibleTimes(postPossibleTimes)
                .openChatUrl(openChatUrl)
                .togetherActivity(togetherActivity)
                .postImages(postImages)
                .postStatus(postStatus)
                .build();
        return post1;
     }
}