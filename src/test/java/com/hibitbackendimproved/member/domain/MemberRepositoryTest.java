package com.hibitbackendimproved.member.domain;

import com.hibitbackendimproved.IntegrationTestSupport;
import com.hibitbackendimproved.member.exception.NotFoundMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시;
import static com.hibitbackendimproved.common.fixtures.MemberFixtures.팬시_소셜로그인_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("소셜_로그인ID을 통해 회원을 찾는다.")
    @Test
    void 소셜_로그인ID을_통해_회원을_찾는다() {
        // given
        Member 팬시 = memberRepository.save(팬시());

        // when
        Member actual = memberRepository.getBySocialIdOrThrow(팬시_소셜로그인_ID);

        // then
        assertThat(actual.getId()).isEqualTo(팬시.getId());
    }

    @DisplayName("중복된 소셜_로그인ID이 존재하는 경우 true를 반환한다.")
    @Test
    void 중복된_소셜_로그인ID이_존재하는_경우_true를_반환한다() {
        // given
        memberRepository.save(팬시());

        // when
        boolean actual = memberRepository.existsBySocialId(팬시_소셜로그인_ID);

        // then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("존재하지 않는 소셜_로그인ID을 조회할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_email을_조회할_경우_예외를_던진다() {
        // given
        final String socialId = "123456789";

        // given & when & then
        assertThatThrownBy(() -> memberRepository.getBySocialIdOrThrow("123456789"))
                .isInstanceOf(NotFoundMemberException.class);
    }

    @DisplayName("존재하지 않는 id이면 예외를 던진다.")
    @Test
    void 존재하지_않는_id이면_예외를_던진다() {
        // given
        Long id = 0L;

        // when & then
        assertThatThrownBy(() -> memberRepository.validateExistById(id))
                .isInstanceOf(NotFoundMemberException.class);
    }
}
