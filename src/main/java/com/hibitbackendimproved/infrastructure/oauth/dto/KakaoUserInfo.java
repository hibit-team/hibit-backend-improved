package com.hibitbackendimproved.infrastructure.oauth.dto;

import lombok.Getter;

@Getter
public class KakaoUserInfo {

    private String nickname;
    private String profileImage;

    private KakaoUserInfo() {
    }

    public KakaoUserInfo(final String nickname, final String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
