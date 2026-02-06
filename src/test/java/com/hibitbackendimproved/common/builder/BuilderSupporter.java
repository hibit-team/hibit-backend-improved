package com.hibitbackendimproved.common.builder;

import com.hibitbackendimproved.auth.domain.OAuthTokenRepository;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.profile.domain.ProfileRepository;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    private final MemberRepository memberRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final ProfileRepository profileRepository;

    public BuilderSupporter(final MemberRepository memberRepository,
                            final OAuthTokenRepository oAuthTokenRepository,
                            final ProfileRepository profileRepository) {
        this.memberRepository = memberRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.profileRepository = profileRepository;
    }

    public MemberRepository memberRepository() {
        return memberRepository;
    }

    public OAuthTokenRepository oAuthTokenRepository() {
        return oAuthTokenRepository;
    }

    public ProfileRepository profileRepository() {
        return profileRepository;
    }
}
