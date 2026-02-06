package com.hibitbackendimproved.post.dto.response;

import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailResponse {
    private Long id;
    private Long writerId;
    private String title;
    private String content;
    private String exhibition;
    private String exhibitionImage;
    private String openChatUrl;
    private PostStatus postStatus;
    private int viewCount;

    @Builder
    public PostDetailResponse(final Long id, final Long writerId
            , final String title, final String content, final String exhibition, final String exhibitionImage
            , final String openChatUrl, final PostStatus postStatus, int viewCount) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.exhibition = exhibition;
        this.exhibitionImage = exhibitionImage;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
        this.viewCount = viewCount;
    }

    public static PostDetailResponse of(final Post post, final LoginMember loginMember) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .writerId(loginMember.getId())
                .title(post.getTitle().getValue())
                .exhibitionImage(post.getExhibitionImage())
                .content(post.getContent().getValue())
                .exhibition(post.getExhibition().getValue())
                .openChatUrl(post.getOpenChatUrl())
                .postStatus(post.getPostStatus())
                .viewCount(post.getViewCount())
                .build();
    }
}
