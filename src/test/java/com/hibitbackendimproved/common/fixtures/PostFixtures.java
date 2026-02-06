package com.hibitbackendimproved.common.fixtures;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import com.hibitbackendimproved.post.domain.vo.Exhibition;
import com.hibitbackendimproved.post.dto.response.PostDetailResponse;

public class PostFixtures {


    /* 게시글1 : 해시태크 */
    public static final Long 게시글_ID = 1L;
    public static final Long 로그인한_사용자_ID = 1L;

    public static final String 게시글제목 = "프로젝트_해시테크";
    public static final String 게시글내용 = "프로젝트 해시 태크(http://projecthashtag.net/) 보러가실 분 있으면 아래 댓글 남겨주세요~";
    public static final String 전시회링크 = "http://projecthashtag.net/";
    public static final String 전시회제목 = "PROJECT HASHTAG 2023 SELECTED ARTISTS";
    public static final String 전시회이미지 = "exhibitionImage.png";
    public static final String 전시회장소 = "국립현대미술관 서울";
    public static final int 전시회가격 = 0; // 무료

    public static final String 오픈채팅방Url = "http://openchat.com/1";
    public static final PostStatus 모집상태 = PostStatus.HOLDING;
    public static final int 게시글_조회수 = 0;

    /* 게시글2: 오스틴리 전시회 */
    public static final String 게시글제목2 = "오스틴리 전시회";
    public static final String 게시글내용2 = "오스틴리 전시회 보고, 카페에서 같이 담소하게 얘기 나누실 분 있으시면 아래 댓글 남겨주세요~";

    public static final String 전시회링크2 = "http://ostin.net/";
    public static final String 전시회제목2 = "오스틴리 전시회";
    public static final String 전시회이미지2 = "exhibitionImage2.png";
    public static final String 전시회장소2 = "예술의 전당";
    public static final int 전시회가격2 = 15000;

    public static final String 오픈채팅방Url2 = "http://openchat.com/2";
    public static final PostStatus 모집상태2 = PostStatus.HOLDING;

    public static PostDetailResponse 프로필_등록_응답() {
        return PostDetailResponse.builder()
                .id(게시글_ID)
                .writerId(로그인한_사용자_ID)
                .title(게시글제목2)
                .content(게시글내용2)
                .exhibitionLink(전시회링크2)
                .exhibitionTitle(전시회제목2)
                .exhibitionImage(전시회이미지2)
                .exhibitionPlace(전시회장소2)
                .exhibitionPrice(전시회가격2)
                .openChatUrl(오픈채팅방Url2)
                .postStatus(모집상태2)
                .viewCount(게시글_조회수)
                .build();
    }

    public static Post 프로젝트_해시테크(final Member member) {
        return Post.builder()
                .member(member)
                .title(게시글제목)
                .content(게시글내용)
                .exhibition(new Exhibition(전시회링크, 전시회제목, 전시회이미지, 전시회장소, 전시회가격))
                .openChatUrl(오픈채팅방Url)
                .postStatus(모집상태)
                .build();
    }

    public static Post 오스틴리_전시회(final Member member) {
        return Post.builder()
                .member(member)
                .title(게시글제목2)
                .content(게시글내용2)
                .exhibition(new Exhibition(전시회링크2, 전시회제목2, 전시회이미지2, 전시회장소2, 전시회가격2))
                .openChatUrl(오픈채팅방Url2)
                .postStatus(모집상태2)
                .build();
    }
}
