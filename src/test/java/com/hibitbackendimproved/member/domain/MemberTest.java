package com.hibitbackendimproved.member.domain;

import com.hibitbackendimproved.member.exception.InvalidMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시_닉네임;
import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시_이메일;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberTest {
    @DisplayName("회원을 생성한다.")
    @Test
    void 회원을_생성한다() {

        // given & when & then
        assertDoesNotThrow(() -> new Member(팬시_이메일, 팬시_닉네임, SocialType.KAKAO));
    }

    @DisplayName("회원의 email 형식이 맞지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"fancy.junyongmoon@", "fancy.junyongmoon@amail", "fancy.junyongmoon"})
    void 회원의_email_형식이_맞지_않으면_예외가_발생한다(final String email) {

        // given & when & then
        assertThatThrownBy(() -> new Member(email, 팬시_닉네임, SocialType.KAKAO))
            .isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("회원의 닉네임을 수정한다.")
    @Test
    void 회원의_닉네임을_수정한다() {
        // given
        final Member member = new Member(팬시_이메일, 팬시_닉네임, SocialType.KAKAO);
        final String updatedNickname = "잠만보";

        // when
        member.updateNickname(updatedNickname);

        // then
        assertThat(member.getNickname()).isEqualTo(updatedNickname);
    }

    @DisplayName("회원의 닉네임 형식이 잘못되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 회원의_닉네임_형식이_빈칸이거나_공백이면_예외가_발생한다(final String nickname) {

        // given & when & then
        assertThatThrownBy(() -> new Member(팬시_이메일, nickname, SocialType.KAKAO))
                .isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("회원의 닉네임 글자가 20자 초과하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일", "일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십"})
    void 회원의_닉네임_글자가_20자_초과하면_예외가_발생한다(final String nickname) {

        // given & when & then
        assertThatThrownBy(() -> new Member(팬시_이메일, nickname, SocialType.KAKAO))
                .isInstanceOf(InvalidMemberException.class);
     }
}
