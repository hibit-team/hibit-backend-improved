package com.hibitbackendimproved.member.application;

import com.hibitbackendimproved.IntegrationTestSupport;
import com.hibitbackendimproved.auth.domain.OAuthToken;
import com.hibitbackendimproved.auth.domain.OAuthTokenRepository;
import com.hibitbackendimproved.common.builder.BuilderSupporter;
import com.hibitbackendimproved.common.builder.GivenBuilder;
import com.hibitbackendimproved.config.ExternalApiConfig;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.member.domain.SocialType;
import com.hibitbackendimproved.profile.domain.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_닉네임;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ExternalApiConfig.class)
@ActiveProfiles("test")
class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BuilderSupporter builderSupporter;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAllInBatch();
        oAuthTokenRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    private Member member;

    @DisplayName("서비스에 가입한 회원을 조회한다. (수정 전)")
    @Test
    void 서비스에_가입한_회원을_조회한다_수정전() {
        // given
        Member member = new Member("123456789", SocialType.KAKAO);
        this.member = memberRepository.save(member);
        OAuthToken oAuthToken = new OAuthToken(this.member, "refreshTokenValue");
        oAuthTokenRepository.save(oAuthToken);

        // when & then
        Assertions.assertThat(memberService.findById(member.getId()).getId())
                .isEqualTo(member.getId());
     }

    @DisplayName("서비스에 가입한 회원을 조회한다. (수정 후)")
    @Test
    void 서비스에_가입된_회원을_조회한다() {
        // given
        GivenBuilder 팬시 = 팬시();

        // when & then
        assertThat(memberService.findById(팬시.회원().getId()).getId())
                .isEqualTo(팬시.회원().getId());
    }

    protected GivenBuilder 팬시() {
        GivenBuilder 팬시 = new GivenBuilder(builderSupporter);
        팬시.회원_가입을_한다("123456789", 팬시_닉네임);
        return 팬시;
    }
}
