package com.hibitbackendimproved.common.fixtures;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;
import com.hibitbackendimproved.member.dto.MemberResponse;

public class MemberFixtures {

    /* 팬시 */
    public static final Long FANCY_ID = 1L;
    public static final String 팬시_이메일 = "fancy@gmail.com";
    public static final String 팬시_닉네임 = "팬시";
    public static final SocialType 소셜로그인유형 = SocialType.KAKAO;

    public static final MemberResponse 팬시_응답 = new MemberResponse(1L, 팬시_이메일, 팬시_닉네임, 소셜로그인유형);

    public static Member 팬시() {
        return Member.builder()
                .email(팬시_이메일)
                .displayName(팬시_닉네임)
                .socialType(소셜로그인유형)
                .build();
    }
}
