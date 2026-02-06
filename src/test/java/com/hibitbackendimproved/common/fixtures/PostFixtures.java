package com.hibitbackendimproved.common.fixtures;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import com.hibitbackendimproved.post.domain.TogetherActivity;
import com.hibitbackendimproved.post.dto.response.PostDetailResponse;

import java.time.LocalDateTime;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.*;
import static com.hibitbackendimproved.post.dto.response.PostResponse.AttendanceAndTogetherActivity;

public class PostFixtures {


    /* 게시글1 : 해시태크 */
    public static final Long 게시글_ID1 = 1L;

    public static final Long 로그인한_사용자_ID = 1L;

    public static final String 게시글_작성한_사용자_닉네임 = "팬시";
    public static final String 게시글제목1 = "프로젝트_해시테크";
    public static final String 게시글내용1 = "프로젝트 해시 태크(http://projecthashtag.net/) 보러가실 분 있으면 아래 댓글 남겨주세요~";

    public static final String 전시회제목1 = "PROJECT HASHTAG 2023 SELECTED ARTISTS";

    public static final int 전시관람인원1 = 3;
    public static final LocalDateTime 전시관람희망날짜1 = LocalDateTime.now().withNano(0);
    public static final String 오픈채팅방Url1 = "http://projecthashtag.net/";
    public static final TogetherActivity 함께하고싶은활동1 = TogetherActivity.EAT;

    public static final String 게시글이미지1 = "postImage1.png";
    public static final PostStatus 모집상태1 = PostStatus.HOLDING;
    public static final int 게시글_조회수1 = 0;

    /* 게시글1-2 : 해시태크-2 */
    public static final String 게시글제목1_2 = "프로젝트_해시테크";
    public static final String 게시글내용1_2 = "프로젝트 해시 태크(http://projecthashtag.net/) 보러가실 분 있으면 아래 댓글 남겨주세요~";

    public static final String 전시회제목1_2 = "PROJECT HASHTAG 2023 SELECTED ARTISTS";

    public static final int 전시관람인원1_2 = 3;
    public static final LocalDateTime 전시관람희망날짜1_2 = LocalDateTime.now();
    public static final String 오픈채팅방Url1_2 = "http://projecthashtag.net/";
    public static final TogetherActivity 함께하고싶은활동1_2 = TogetherActivity.EAT;

    public static final String 게시글이미지1_2 = "postImage1.png";
    public static final PostStatus 모집상태1_2 = PostStatus.HOLDING;


    /* 게시글2: 오스틴리 전시회 */
    public static final String 게시글제목2 = "오스틴리 전시회";
    public static final String 게시글내용2 = "오스틴리 전시회 보고, 카페에서 같이 담소하게 얘기 나누실 분 있으시면 아래 댓글 남겨주세요~";

    public static final String 전시회제목2 = "오스틴리 전시회";

    public static final int 전시관람인원2 = 4;
    public static final LocalDateTime 전시관람희망날짜2 = LocalDateTime.now().plusDays(1).withNano(0);

    public static final String 오픈채팅방Url2 = "http://ostin.net/";
    public static final TogetherActivity 함께하고싶은활동2 = TogetherActivity.CAFE;

    public static final String 게시글이미지2 = "postImage2.png";

    public static final PostStatus 모집상태2 = PostStatus.HOLDING;

    public static PostDetailResponse 프로필_등록_응답() {
        return new PostDetailResponse(게시글_ID1, 로그인한_사용자_ID, 게시글_작성한_사용자_닉네임, 게시글제목1, 게시글내용1, 전시회제목1, AttendanceAndTogetherActivity(전시관람인원1, 함께하고싶은활동1), 전시관람희망날짜1, 오픈채팅방Url1, 모집상태1, 게시글이미지1, 게시글_조회수1);
    }

    public static Post 프로젝트_해시테크_2(Member member) {
        return Post.builder()
                .member(member)
                .title(게시글제목1_2)
                .content(게시글내용1_2)
                .exhibition(전시회제목1_2)
                .exhibitionAttendance(전시관람인원1_2)
                .possibleTime(전시관람희망날짜1_2)
                .openChatUrl(오픈채팅방Url1_2)
                .togetherActivity(함께하고싶은활동1_2)
                .imageName(게시글이미지1_2)
                .postStatus(모집상태1_2)
                .build();
    }

    public static Post 프로젝트_해시테크(Member member) {
        return Post.builder()
                .member(member)
                .title(게시글제목1)
                .content(게시글내용1)
                .exhibition(전시회제목1)
                .exhibitionAttendance(전시관람인원1)
                .possibleTime(전시관람희망날짜1)
                .openChatUrl(오픈채팅방Url1)
                .togetherActivity(함께하고싶은활동1)
                .imageName(게시글이미지1)
                .postStatus(모집상태1)
                .build();
    }

    public static Post 프로젝트_해시테크() {
        return Post.builder()
                .member(팬시())
                .title(게시글제목1)
                .content(게시글내용1)
                .exhibition(전시회제목1)
                .exhibitionAttendance(전시관람인원1)
                .possibleTime(전시관람희망날짜1)
                .openChatUrl(오픈채팅방Url1)
                .togetherActivity(함께하고싶은활동1)
                .imageName(게시글이미지1)
                .postStatus(모집상태1)
                .build();
    }

    public static Post 오스틴리_전시회(Member member) {
        return Post.builder()
                .member(member)
                .title(게시글제목2)
                .content(게시글내용2)
                .exhibition(전시회제목2)
                .exhibitionAttendance(전시관람인원2)
                .possibleTime(전시관람희망날짜2)
                .openChatUrl(오픈채팅방Url2)
                .togetherActivity(함께하고싶은활동2)
                .imageName(게시글이미지2)
                .postStatus(모집상태2)
                .build();
    }
}
