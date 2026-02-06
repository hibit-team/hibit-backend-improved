package com.hibitbackendimproved.common;


import com.hibitbackendimproved.auth.dto.OAuthMember;
import com.hibitbackendimproved.member.domain.SocialType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum OAuthFixtures {
    관리자("관리자", 관리자()),
    팬시("팬시", 팬시()),

    MEMBER("member authorization code", MEMBER()),
    CREATOR("creator authorization code", CREATOR());

    private String code;
    private OAuthMember oAuthMember;

    OAuthFixtures(final String code, final OAuthMember oAuthMember) {
        this.code = code;
        this.oAuthMember = oAuthMember;
    }

    public static OAuthMember parseOAuthMember(final String code) {
        OAuthFixtures oAuthFixtures = Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return oAuthFixtures.oAuthMember;
    }

    private static OAuthMember 관리자() {
        final String 관리자_소셜_로그인_ID = "admin123";
        final String 관리자_닉네임 = "관리자";
        final String 관리자_프로필_이미지 = "admin.png";
        final String 관리자_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(관리자_소셜_로그인_ID, SocialType.KAKAO, 관리자_REFRESH_TOKEN, 관리자_닉네임, 관리자_프로필_이미지);
    }

    private static OAuthMember 팬시() {
        final String 팬시_소셜_로그인_ID = "fancy123";
        final String 팬시_닉네임 = "팬시";
        final String 팬시_프로필_이미지 = "fancy.png";
        final String 팬시_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(팬시_소셜_로그인_ID, SocialType.KAKAO, 팬시_REFRESH_TOKEN, 팬시_닉네임, 팬시_프로필_이미지);
    }

    private static OAuthMember MEMBER() {
        final String MEMBER_소셜_로그인_ID = "member123";
        final String MEMBER_닉네임 = "member";
        final String MEMBER_프로필_이미지 = "member.png";
        final String MEMBER_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";
        return new OAuthMember(MEMBER_소셜_로그인_ID, SocialType.KAKAO, MEMBER_REFRESH_TOKEN, MEMBER_닉네임, MEMBER_프로필_이미지);
    }

    private static OAuthMember CREATOR() {
        final String CREATOR_소셜_로그인_ID = "member123";
        final String CREATOR_닉네임 = "creator";
        final String CREATOR_프로필_이미지 = "creator.png";
        final String CREATOR_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";
        return new OAuthMember(CREATOR_소셜_로그인_ID, SocialType.KAKAO, CREATOR_REFRESH_TOKEN, CREATOR_닉네임, CREATOR_프로필_이미지);
    }

    public String getCode() {
        return code;
    }

    public OAuthMember getOAuthMember() {
        return oAuthMember;
    }
}
