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
    private String exhibition;
    private String exhibitionImage;
    private PostStatus postStatus;
    private LocalDateTime createDateTime;

    @Builder
    public PostResponse(final Long id, final String title
            , final String exhibition, final String exhibitionImage
            , final PostStatus postStatus, final LocalDateTime createDateTime) {
        this.id = id;
        this.title = title;
        this.exhibition = exhibition;
        this.exhibitionImage = exhibitionImage;
        this.postStatus = postStatus;
        this.createDateTime = createDateTime;
    }

    public static PostResponse from(final Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle().getValue())
                .exhibition(post.getExhibition().getValue())
                .exhibitionImage(post.getExhibitionImage())
                .postStatus(post.getPostStatus())
                .createDateTime(post.getCreateAt())
                .build();
    }
}
