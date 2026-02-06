package com.hibitbackendimproved.post.dto.request;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateRequest {

    private String title;
    private String content;
    private String exhibition;
    private String exhibitionImage;
    private String openChatUrl;
    private PostStatus postStatus;

    @Builder
    public PostCreateRequest(final String title, final String content,
                             final String exhibition, final String exhibitionImage,
                             final String openChatUrl, final PostStatus postStatus) {
        this.title = title;
        this.content = content;
        this.exhibition = exhibition;
        this.exhibitionImage = exhibitionImage;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
    }

    public Post toEntity(final Member foundMember, final PostCreateRequest request) {
        return Post.builder()
                .member(foundMember)
                .title(request.getTitle())
                .content(request.getContent())
                .exhibition(request.getExhibition())
                .exhibitionImage(request.getExhibitionImage())
                .openChatUrl(request.getOpenChatUrl())
                .postStatus(PostStatus.HOLDING)
                .build();
    }
}

