package com.hibitbackendimproved.post.dto.request;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import com.hibitbackendimproved.post.domain.vo.Exhibition;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateRequest {

    private String title;
    private String content;

    private String exhibitionLink;
    private String exhibitionTitle;
    private String exhibitionImage;
    private String exhibitionPlace;
    private int exhibitionPrice;

    private String openChatUrl;
    private PostStatus postStatus;

    @Builder
    public PostCreateRequest(final String title, final String content,
                             final String exhibitionLink, final String exhibitionTitle, final String exhibitionImage, final String exhibitionPlace, final int exhibitionPrice,
                             final String openChatUrl, final PostStatus postStatus) {
        this.title = title;
        this.content = content;
        this.exhibitionLink = exhibitionLink;
        this.exhibitionTitle = exhibitionTitle;
        this.exhibitionImage = exhibitionImage;
        this.exhibitionPlace = exhibitionPlace;
        this.exhibitionPrice = exhibitionPrice;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
    }

    public Post toEntity(final Member member) {
        return Post.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .exhibition(new Exhibition(exhibitionLink, exhibitionTitle, exhibitionImage, exhibitionPlace, exhibitionPrice))
                .openChatUrl(this.openChatUrl)
                .postStatus(this.postStatus)
                .build();
    }
}

