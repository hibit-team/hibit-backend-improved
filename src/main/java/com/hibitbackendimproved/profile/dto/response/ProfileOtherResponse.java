package com.hibitbackendimproved.profile.dto.response;

import com.hibitbackendimproved.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileOtherResponse {
    private String nickname;
    private String profileImage;
    private String introduce;

    @Builder
    public ProfileOtherResponse(final String nickname, final String profileImage, final String introduce) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.introduce = introduce;
    }

    public static ProfileOtherResponse of(final Profile profile) {
        return ProfileOtherResponse.builder()
                .nickname(profile.getNickname())
                .profileImage(profile.getProfileImage())
                .introduce(profile.getIntroduce())
                .build();

    }
}
