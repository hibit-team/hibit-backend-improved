package com.hibitbackendimproved.profile.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileUpdateRequest {

    private String nickname;
    private String profileImage;
    private String introduce;

    @Builder
    public ProfileUpdateRequest(final String nickname, final String profileImage, final String introduce) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.introduce = introduce;
    }
}
