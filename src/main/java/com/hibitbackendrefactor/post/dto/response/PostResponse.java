package com.hibitbackendrefactor.post.dto.response;

import com.hibitbackendrefactor.post.domain.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {
    private Long id;
    private String title;
    private String exhibition;
    private List<String> exhibitionAttendanceAndTogetherActivity;
    private PostStatus postStatus;
    private String postImage;
    private LocalDate createDateTime;


    @Builder
    public PostResponse(Long id, String title, String exhibition
            , List<String> exhibitionAttendanceAndTogetherActivity, PostStatus postStatus
            , String postImage, LocalDate createDateTime) {
        this.id = id;
        this.title = title;
        this.exhibition = exhibition;
        this.exhibitionAttendanceAndTogetherActivity = exhibitionAttendanceAndTogetherActivity;
        this.postStatus = postStatus;
        this.postImage = postImage;
        this.createDateTime = createDateTime;
    }

    public static PostResponse of(final Post post, final String imageUrl) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .exhibition(post.getExhibition())
                .exhibitionAttendanceAndTogetherActivity(AttendanceAndTogetherActivity(post.getExhibitionAttendance(), post.getTogetherActivity()))
                .postStatus(post.getPostStatus())
                .postImage(imageUrl)
                .createDateTime(post.getCreateDateTime().toLocalDate())
                .build();
    }
    public static List<String> AttendanceAndTogetherActivity(int exhibitionAttendance, TogetherActivity togetherActivity) {
        List<String> attendanceAndTogetherActivity = new ArrayList<>();
        attendanceAndTogetherActivity.add(exhibitionAttendance + "인 관람");
        attendanceAndTogetherActivity.add(togetherActivity.getText());
        return attendanceAndTogetherActivity;
    }
}
