package com.hibitbackendimproved.profile.domain;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.profile.exception.InvalidProfileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_닉네임;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_이미지;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_자기소개;
import static com.hibitbackendimproved.common.fixtures.ProfileFixtures.팬시_프로필;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProfileTest {

    @Test
    void 프로필을_생성한다() {
        // given
        Member 팬시 = 팬시();

        // when & then
        assertDoesNotThrow(() -> new Profile(팬시, 팬시_닉네임, 팬시_이미지, 팬시_자기소개));
    }

    @ParameterizedTest
    @ValueSource(strings = {"팬시", "fancy", "devfancy"})
    void 닉네임이_1자이상_20자_이하이면_성공한다(String nickname) {
        // given
        Member 팬시 = 팬시();

        // when & then
        assertDoesNotThrow(() -> new Profile(팬시, nickname, 팬시_이미지, 팬시_자기소개));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "일이삼사오육칠팔구십"
            + "일이삼사오육칠팔구십" + "일"})
    void 닉네임이_공백이거나_20자_이상_초과하면_예외를_던진다(final String 잘못된_닉네임) {
        // given
        Member 팬시 = 팬시();

        // when & then
        // isBlank - 문자열이 null or 비어있거나 or 공백 문자가 포함되는 경우 (Java 11)
        assertThatThrownBy(
                () -> new Profile(팬시, 잘못된_닉네임, 팬시_이미지, 팬시_자기소개)
        ).isInstanceOf(InvalidProfileException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"일", "안녕하세요", "안녕하세요. 저는 개발자 팬시입니다. 취미는 운동, 독서입니다. 잘부탁드립니다."})
    void 자기소개의_길이가_1자_이상_200자_이하인_경우_성공한다(String introduce) {
        // given
        final Member 팬시 = 팬시();

        // when & then
        assertDoesNotThrow(() -> new Profile(팬시, 팬시_닉네임, 팬시_이미지, introduce));
    }

    @Test
    void 자기소개의_길이가_200을_초과하는_경우_예외를_던진다() {
        // given
        final Member 팬시 = 팬시();
        final String 잘못된_자기소개 = "1".repeat(201);

        // when & then
        assertThatThrownBy(
                () -> new Profile(팬시, 팬시_닉네임, 팬시_이미지, 잘못된_자기소개))
                .isInstanceOf(InvalidProfileException.class);
    }

    @Test
    void 본인의_프로필에서_자기소개를_변경한다() {
        // given
        final Profile profile = 팬시_프로필();
        final String introduce = "안녕하세요 서버 개발자로 살아가는 팬시입니다.";

        // when
        profile.updateIntroduce(introduce);

        // then
        assertThat(profile.getIntroduce()).isEqualTo(introduce);
    }

    @Test
    void 본인의_프로필에서_이미지를_변경한다() {
        // given
        final Profile profile = 팬시_프로필();
        final String profileImage = "devfancy.png";

        // when
        profile.updateProfileImage(profileImage);

        // then
        assertThat(profile.getProfileImage()).isEqualTo(profileImage);
    }

}
