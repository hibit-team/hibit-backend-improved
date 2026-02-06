package com.hibitbackendimproved.auth.dto;


import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;
import lombok.Getter;

@Getter
public class OAuthMember {

    private String socialId;
    private SocialType socialType;
    private final String refreshToken;
    private final String nickname;
    private final String profileImage;

    public OAuthMember(final String socialId, final SocialType socialType, final String refreshToken, final String nickname, final String profileImage) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public Member toMember() {
        return new Member(socialId, socialType);
    }
}
