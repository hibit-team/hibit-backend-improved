package com.hibitbackendimproved.profile.domain;

import com.hibitbackendimproved.IntegrationTestSupport;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_프로필;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ProfileRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Profile profile1;

    private Member member1;

    @BeforeEach
    void setUp() {
        member1 = 팬시();
        memberRepository.save(member1);
        profile1 = 팬시_프로필(member1);
        profileRepository.save(profile1);
    }

    @DisplayName("프로필과 회원 테이블이 정상적으로 매핑이 된다.")
    @Test
    void 프로필과_회원_테이블이_정상적으로_매핑이_된다() {
        // given
        Profile foundProfile = profileRepository.getByIdOrThrow(profile1.getId());

        // when & then
        assertThat(foundProfile.getMember()).isNotNull();
    }

    @DisplayName("특정 회원 ID에 해당하는 프로필을 찾는다.")
    @Test
    void 특정_회원_ID에_해당하는_프로필을_찾는다() {
        // given
        Long memberId = profile1.getMember().getId();

        // when
        Optional<Profile> actual = profileRepository.findByMemberId(memberId);

        // then
        assertThat(actual).isPresent();
        Profile foundProfile = actual.get();
        assertThat(foundProfile.getMember().getId()).isEqualTo(memberId);
    }

    @DisplayName("이미 존재하는 닉네임인지 확인한다.")
    @Test
    void 이미_존재하는_닉네임인지_확인한다() {
        // given
        String existingNickname = profile1.getNickname();

        // when
        boolean existsByNickname = profileRepository.existsByNickname(existingNickname);

        // then
        assertThat(existsByNickname).isTrue();
    }

    @DisplayName("존재하지 않는 닉네임인지 확인한다.")
    @Test
    void 존재하지_않는_닉네임인지_확인한다() {
        // given
        String nonExistingNickname = "nonExistingNickname";

        // when
        boolean existsByNickname = profileRepository.existsByNickname(nonExistingNickname);

        // then
        assertThat(existsByNickname).isFalse();
    }
}
