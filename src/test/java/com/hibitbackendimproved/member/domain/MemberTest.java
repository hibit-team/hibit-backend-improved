package com.hibitbackendimproved.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberTest {
    @DisplayName("회원을 생성한다.")
    @Test
    void 회원을_생성한다() {

        // given & when & then
        assertDoesNotThrow(() -> new Member("123456789", SocialType.KAKAO));
    }
}
