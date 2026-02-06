package com.hibitbackendimproved.post.dto.request;

import com.hibitbackendimproved.post.domain.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

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
    public PostUpdateRequest(final String title, final String content,
                             final String exhibitionTitle, final String exhibitionLink,
                             final String exhibitionImage, final String exhibitionPlace, final int exhibitionPrice,
                             final String openChatUrl, final PostStatus postStatus) {
        this.title = title;
        this.content = content;
        this.exhibitionTitle = exhibitionTitle;
        this.exhibitionLink = exhibitionLink;
        this.exhibitionImage = exhibitionImage;
        this.exhibitionPlace = exhibitionPlace;
        this.exhibitionPrice = exhibitionPrice;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
    }

    public PostUpdateServiceRequest toServiceRequest() {
        return PostUpdateServiceRequest.builder()
                .title(title)
                .content(content)
                .exhibitionTitle(exhibitionTitle)
                .exhibitionLink(exhibitionLink)
                .exhibitionImage(exhibitionImage)
                .exhibitionPlace(exhibitionPlace)
                .exhibitionPrice(exhibitionPrice)
                .openChatUrl(openChatUrl)
                .postStatus(postStatus)
                .build();
    }
}
