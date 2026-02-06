package com.hibitbackendimproved.common.fixtures;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;
import com.hibitbackendimproved.member.dto.MemberResponse;

public class MemberFixtures {

    /* 팬시 */
    public static final Long FANCY_ID = 1L;
    public static final String 팬시_소셜로그인_ID = "123456789";
    public static final SocialType 소셜로그인유형 = SocialType.KAKAO;

    public static final MemberResponse 팬시_응답 = new MemberResponse(1L, 팬시_소셜로그인_ID, 소셜로그인유형);

    public static Member 팬시() {
        return Member.builder()
                .socialId(팬시_소셜로그인_ID)
                .socialType(소셜로그인유형)
                .build();
    }
}
