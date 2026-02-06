package com.hibitbackendimproved.common.builder;

import com.hibitbackendimproved.auth.domain.OAuthToken;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;
import com.hibitbackendimproved.profile.domain.Profile;

public class GivenBuilder {

    private final BuilderSupporter bs;
    private Member member;

    public GivenBuilder(BuilderSupporter bs) {
        this.bs = bs;
    }

    public GivenBuilder 회원_가입을_한다(final String socialId, final String nickname){
        Member member = new Member(socialId, SocialType.KAKAO);
        this.member = bs.memberRepository().save(member);

        OAuthToken oAuthToken = new OAuthToken(this.member, "refreshTokenValue");
        bs.oAuthTokenRepository().save(oAuthToken);

        Profile profile = new Profile(this.member, nickname, "profile_image.jpg", null);
        bs.profileRepository().save(profile);

        return this;
    }

    public Member 회원() {
        return member;
    }
}
