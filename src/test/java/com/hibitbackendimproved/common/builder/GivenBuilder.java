package com.hibitbackendimproved.common.builder;

import com.hibitbackendimproved.auth.domain.OAuthToken;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;

public class GivenBuilder {
    private final BuilderSupporter bs;

    private Member member;

    public GivenBuilder(BuilderSupporter bs) {
        this.bs = bs;
    }

    public GivenBuilder 회원_가입을_한다(final String email, final String displayName) {
        Member member = new Member(email, displayName, SocialType.KAKAO);
        this.member = bs.memberRepository().save(member);
        OAuthToken oAuthToken = new OAuthToken(this.member, "refreshTokenValue");
        bs.oAuthTokenRepository().save(oAuthToken);
        return this;
    }

    public Member 회원() {
        return member;
    }
}
