package com.hibitbackendimproved.post.domain;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.post.domain.vo.Exhibition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.게시글내용;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.게시글내용2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.게시글제목;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.게시글제목2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.모집상태;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.모집상태2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.오스틴리_전시회;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.오픈채팅방Url;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.오픈채팅방Url2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회가격;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회가격2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회링크;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회링크2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회이미지;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회이미지2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회장소;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회장소2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회제목;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.전시회제목2;
import static com.hibitbackendimproved.common.fixtures.PostFixtures.프로젝트_해시테크;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PostTest {

    @DisplayName("게시글을 등록한다. - Case 1 ")
    @Test
    void 게시글을_등록한다_Case1() {
        // given
        Member 팬시 = 팬시();

        // when & then
        assertDoesNotThrow(() -> 프로젝트_해시테크(팬시));
    }

    @DisplayName("게시글을 등록한다. - Case 2")
    @Test
    void 게시글을_등록한다_Case2() {
        // given
        Member 팬시 = 팬시();
        Exhibition exhibition = new Exhibition(전시회제목, 전시회링크, 전시회이미지, 전시회장소, 전시회가격);

        // when & then
        assertDoesNotThrow(() -> new Post(팬시, 게시글제목, 게시글내용
                , exhibition, 오픈채팅방Url, 모집상태));
    }

    @DisplayName("게시글을 작성한 회원 정보를 가져온다.")
    @Test
    void 게시글을_작성한_회원_정보를_가져온다() {
        // given
        Member 팬시 = 팬시();
        Post post = 오스틴리_전시회(팬시);

        // when
        Member foundMember = post.getMember();

        // then
        assertThat(foundMember).isEqualTo(팬시);
    }

    @DisplayName("새로 생성한 게시글에서 가져온 정보가 일치하는지 확인한다.")
    @Test
    void 새로_생성한_게시글에서_가져온_정보가_일치하는지_확인한다() {
        // given
        Member 팬시 = 팬시();
        Post post = 오스틴리_전시회(팬시);

        // when & then
        assertAll(
                () -> assertThat(post.getTitle().getValue()).isEqualTo(게시글제목2),
                () -> assertThat(post.getContent().getValue()).isEqualTo(게시글내용2),
                () -> assertThat(post.getExhibition().getLink()).isEqualTo(전시회링크2),
                () -> assertThat(post.getExhibition().getTitle()).isEqualTo(전시회제목2),
                () -> assertThat(post.getExhibition().getImage()).isEqualTo(전시회이미지2),
                () -> assertThat(post.getOpenChatUrl()).isEqualTo(오픈채팅방Url2),
                () -> assertThat(post.getPostStatus()).isEqualTo(모집상태2)
        );
    }

    @DisplayName("게시글 정보에서 일부 혹은 전체 속성을 수정한다.")
    @Test
    void 게시글_정보에서_일부_혹은_전체_속성을_수정한다() {
        // given
        Member 팬시 = 팬시();
        Post post = 프로젝트_해시테크(팬시);
        Exhibition updatedExhibition = new Exhibition(전시회제목2, 전시회링크2, 전시회이미지2, 전시회장소2, 전시회가격2);

        // when
        post.update(게시글제목2, 게시글내용2, updatedExhibition, 오픈채팅방Url2, 모집상태2);

        // then
        assertAll(
                () -> assertThat(post.getTitle().getValue()).isEqualTo(게시글제목2),
                () -> assertThat(post.getContent().getValue()).isEqualTo(게시글내용2),
                () -> assertThat(post.getExhibition()).isEqualTo(updatedExhibition),

                () -> assertThat(post.getOpenChatUrl()).isEqualTo(오픈채팅방Url2),
                () -> assertThat(post.getPostStatus()).isEqualTo(모집상태2)
        );
    }
}
