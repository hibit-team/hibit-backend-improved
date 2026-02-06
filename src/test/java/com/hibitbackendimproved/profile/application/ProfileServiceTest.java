package com.hibitbackendimproved.profile.application;

import com.hibitbackendimproved.IntegrationTestSupport;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.profile.domain.*;
import com.hibitbackendimproved.profile.dto.request.ProfileCreateRequest;
import com.hibitbackendimproved.profile.dto.request.ProfileUpdateRequest;
import com.hibitbackendimproved.profile.dto.response.ProfileOtherResponse;
import com.hibitbackendimproved.profile.dto.response.ProfileResponse;
import com.hibitbackendimproved.profile.exception.NotFoundProfileException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_프로필;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    void 프로필을_등록한다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        ProfileCreateRequest request = ProfileCreateRequest.builder()
                .nickname("devFancy")
                .profileImage("image.png")
                .introduce("안녕하세요 개발자 팬시입니다.")
                .build();

        // when
        ProfileResponse response = profileService.save(member.getId(), request);
        Profile savedProfile = profileRepository.findByMemberId(팬시.getId()).orElse(null);

        // then
        assertThat(response).isNotNull();
        assertNotNull(savedProfile);
        assertEquals("devFancy", savedProfile.getNickname());
    }

    @Test
    void 본인의_프로필을_조회한다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile profile = 팬시_프로필(member);
        Long memberId = profileRepository.save(profile).getMember().getId();

        // when
        ProfileResponse response = profileService.findMyProfile(memberId);

        // then
        assertAll(
                () -> assertThat(response.getNickname()).isEqualTo(profile.getNickname()),
                () -> assertThat(response.getProfileImage()).isEqualTo(profile.getProfileImage()),
                () -> assertThat(response.getIntroduce()).isEqualTo(profile.getIntroduce())
        );
    }

    @Test
    void 존재하지_않는_프로필을_조회하면_예외가_발생한다() {
        // given
        Long 잘못된_아이디 = 0L;

        // when & then
        assertThatThrownBy(() -> profileService.findMyProfile(잘못된_아이디))
                .isInstanceOf(NotFoundProfileException.class);
    }

    @Test
    void 본인의_프로필을_수정한다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile profile = 팬시_프로필(member);
        Long memberId = profileRepository.save(profile).getMember().getId();

        // when
        ProfileUpdateRequest request = ProfileUpdateRequest.builder()
                .nickname("devFancy2")
                .profileImage("image2.png")
                .introduce("안녕하세요 서버 개발자로 살아가는 팬시입니다.")
                .build();

        profileService.update(memberId, request);
        Profile updatedProfile = profileRepository.findByMemberId(팬시.getId()).orElse(null);

        // then
        assertAll(
                () -> {
                    assertNotNull(updatedProfile);
                    assertThat(updatedProfile.getNickname()).isEqualTo(request.getNickname());
                },
                () -> {
                    assertNotNull(updatedProfile);
                    assertThat(updatedProfile.getProfileImage()).isEqualTo(request.getProfileImage());
                },
                () -> {
                    assertNotNull(updatedProfile);
                    assertThat(updatedProfile.getIntroduce()).isEqualTo(request.getIntroduce());
                }
        );
    }

    @Test
    void 타인의_프로필_정보를_조회한다() {
        // given
        Member 팬시 = 팬시();
        memberRepository.save(팬시);
        Member member = memberRepository.getByIdOrThrow(팬시.getId());

        Profile profile = 팬시_프로필(member);
        Long memberId = profileRepository.save(profile).getMember().getId();

        // when
        ProfileOtherResponse response = profileService.findOtherProfile(memberId);

        // then
        assertNotNull(response);
    }
}
