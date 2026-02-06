package com.hibitbackendimproved.common.fixtures;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.profile.domain.Profile;
import com.hibitbackendimproved.profile.dto.response.ProfileOtherResponse;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;

public class ProfileFixtures {

    /* 팬시 프로필 생성 */
    public static final String 팬시_닉네임 = "팬시";
    public static final String 팬시_이미지 = "fancy.png";
    public static final String 팬시_자기소개 = "안녕하세요. 저는 백엔드 개발자 팬시입니다.";

    /* 팬시 프로필 수정 */
    public static final String 팬시_닉네임2 = "팬시2";
    public static final String 팬시_이미지2 = "fancy2.png";
    public static final String 팬시_자기소개2 = "안녕하세요. 저는 백엔드 개발자 팬시2입니다.";

    /* 브루스 프로필 생성 */
    public static final String 브루스_닉네임 = "브루스";
    public static final String 브루스_이미지 = "bruce.png";
    public static final String 브루스_자기소개 = "안녕하세요. 저는 프론트엔드 개발자 브루스입니다.";

    public static Profile 팬시_프로필() {
        return Profile.builder()
                .member(팬시())
                .nickname(팬시_닉네임)
                .profileImage(팬시_이미지)
                .introduce(팬시_자기소개)
                .build();
    }
    public static Profile 팬시_프로필(final Member 팬시) {
        return Profile.builder()
                .member(팬시)
                .nickname(팬시_닉네임)
                .profileImage(팬시_이미지)
                .introduce(팬시_자기소개)
                .build();
    }
    public static ProfileOtherResponse 타인_프로필_조회_응답() {
        return ProfileOtherResponse.builder()
                .nickname(브루스_닉네임)
                .profileImage(브루스_이미지)
                .introduce(브루스_자기소개)
                .build();
    }
}
