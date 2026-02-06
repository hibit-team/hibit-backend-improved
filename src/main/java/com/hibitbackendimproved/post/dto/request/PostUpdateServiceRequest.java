package com.hibitbackendimproved.post.dto.request;

import com.hibitbackendimproved.post.domain.PostStatus;
import com.hibitbackendimproved.post.domain.vo.Exhibition;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateServiceRequest {
    private String title;
    private String content;

    private String exhibitionTitle;
    private String exhibitionLink;
    private String exhibitionImage;
    private String exhibitionPlace;
    private int exhibitionPrice;

    private String openChatUrl;
    private PostStatus postStatus;

    @Builder
    public PostUpdateServiceRequest(final String title, final String content,
                                    final String exhibitionLink, final String exhibitionTitle,
                                    final String exhibitionImage, final String exhibitionPlace, final int exhibitionPrice,
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

    public Exhibition toExhibition() {
        return new Exhibition(
                this.exhibitionLink,
                this.exhibitionTitle,
                this.exhibitionImage,
                this.exhibitionPlace,
                this.exhibitionPrice
        );
    }
}
