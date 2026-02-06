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
    private String exhibitionTitle;
    private String exhibitionLink;
    private String exhibitionImage;
    private String exhibitionPlace;
    private int exhibitionPrice;
    private String openChatUrl;
    private PostStatus postStatus;
    private int viewCount;

    @Builder
    public PostDetailResponse(final Long id, final Long writerId, final String title, final String content,
                              final String exhibitionTitle, final String exhibitionLink, final String exhibitionImage,
                              final String exhibitionPlace, final int exhibitionPrice,
                              final String openChatUrl, final PostStatus postStatus, final int viewCount) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.exhibitionTitle = exhibitionTitle;
        this.exhibitionLink = exhibitionLink;
        this.exhibitionImage = exhibitionImage;
        this.exhibitionPlace = exhibitionPlace;
        this.exhibitionPrice = exhibitionPrice;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
        this.viewCount = viewCount;
    }

    public static PostDetailResponse of(final Post post, final LoginMember loginMember) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .writerId(loginMember.getId())
                .title(post.getTitle().getValue())
                .content(post.getContent().getValue())
                .exhibitionTitle(post.getExhibition().getTitle())
                .exhibitionLink(post.getExhibition().getLink())
                .exhibitionImage(post.getExhibition().getImage())
                .exhibitionPlace(post.getExhibition().getPlace())
                .exhibitionPrice(post.getExhibition().getPrice())
                .openChatUrl(post.getOpenChatUrl())
                .postStatus(post.getPostStatus())
                .viewCount(post.getViewCount())
                .build();
    }
}
