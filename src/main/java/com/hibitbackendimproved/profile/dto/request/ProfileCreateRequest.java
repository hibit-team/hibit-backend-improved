package com.hibitbackendimproved.profile.dto.request;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileCreateRequest {

    private String nickname;
    private String profileImage;
    private String introduce;


    @Builder
    public ProfileCreateRequest(final String nickname, final String profileImage, final String introduce) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.introduce = introduce;
    }

    public Profile toEntity(final Member foundMember, final ProfileCreateRequest request) {
        return Profile.builder()
                .member(foundMember)
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .introduce(request.getIntroduce())
                .build();

    }
}
