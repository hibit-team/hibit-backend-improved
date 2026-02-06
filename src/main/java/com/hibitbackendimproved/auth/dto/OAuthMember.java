package com.hibitbackendimproved.auth.dto;


import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;

public class OAuthMember {

    private final String email;

    private final String nickname;
    private SocialType socialType;
    private final String refreshToken;
    private final boolean deleted = false;

    public OAuthMember(final String email, final String nickname, final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }

    public OAuthMember(final String email, final String nickname, final SocialType socialType, final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Member toMember() {
        return new Member(email, nickname, socialType);
    }

}
