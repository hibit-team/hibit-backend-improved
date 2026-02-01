package com.hibitbackendimproved.post.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hibitbackendimproved.post.domain.PostStatus;
import com.hibitbackendimproved.post.domain.TogetherActivity;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

    @NotBlank(message = "제목은 1자 이상 30자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "본문은 1자 이상 200자 이하여야 합니다.")
    private String content;

    @NotBlank(message = "전시회 제목은 1자 이상 50자 이하여야 합니다.")
    private String exhibition;

    private int exhibitionAttendance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime possibleTime;
    private String openChatUrl;
    private TogetherActivity togetherActivity;
    private String imageName;
    private PostStatus postStatus;

    @Builder
    public PostUpdateRequest(final String title, final String content, final String exhibition,
                             final int exhibitionAttendance, final LocalDateTime possibleTime, final String openChatUrl,
                             final TogetherActivity togetherActivity, final String imageName, final PostStatus postStatus) {
        this.title = title;
        this.content = content;
        this.exhibition = exhibition;
        this.exhibitionAttendance = exhibitionAttendance;
        this.possibleTime = possibleTime;
        this.openChatUrl = openChatUrl;
        this.togetherActivity = togetherActivity;
        this.imageName = imageName;
        this.postStatus = postStatus;
    }

    public PostUpdateServiceRequest toServiceRequest() {
        return PostUpdateServiceRequest.builder()
                .title(title)
                .content(content)
                .exhibition(exhibition)
                .exhibitionAttendance(exhibitionAttendance)
                .possibleTime(possibleTime)
                .openChatUrl(openChatUrl)
                .togetherActivity(togetherActivity)
                .imageName(imageName)
                .postStatus(postStatus)
                .build();
    }
}
