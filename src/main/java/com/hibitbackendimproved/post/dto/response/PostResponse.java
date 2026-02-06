package com.hibitbackendimproved.post.dto.response;

import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {
    private Long id;
    private String title;
    private String exhibitionTitle;
    private String exhibitionImage;
    private String exhibitionPlace;
    private int exhibitionPrice;
    private PostStatus postStatus;
    private LocalDateTime createDateTime;

    @Builder
    public PostResponse(final Long id, final String title,
                        final String exhibitionTitle, final String exhibitionImage,
                        final String exhibitionPlace, final int exhibitionPrice,
                        final PostStatus postStatus, final LocalDateTime createDateTime) {
        this.id = id;
        this.title = title;
        this.exhibitionTitle = exhibitionTitle;
        this.exhibitionImage = exhibitionImage;
        this.exhibitionPlace = exhibitionPlace;
        this.exhibitionPrice = exhibitionPrice;
        this.postStatus = postStatus;
        this.createDateTime = createDateTime;
    }

    public static PostResponse from(final Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle().getValue())
                .exhibitionTitle(post.getExhibition().getTitle())
                .exhibitionImage(post.getExhibition().getImage())
                .exhibitionPlace(post.getExhibition().getPlace())
                .exhibitionPrice(post.getExhibition().getPrice())
                .postStatus(post.getPostStatus())
                .createDateTime(post.getCreateAt())
                .build();
    }
}
