package com.hibitbackendimproved.post.domain.vo;

import com.hibitbackendimproved.post.exception.InvalidExhibitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExhibitionTest {

    private static final String VALID_LINK = "http://link.com";
    private static final String VALID_IMAGE = "image.jpg";
    private static final String VALID_PLACE = "예술의 전당";
    private static final int VALID_PRICE = 15000;

    @DisplayName("전시회 제목을 생성한다.")
    @Test
    void 전시회_제목을_생성한다() {
        // given
        String title = "오스틴리 전시회";

        // when & then
        assertDoesNotThrow(() -> new Exhibition(VALID_LINK, title, VALID_IMAGE, VALID_PLACE, VALID_PRICE));
     }

    @DisplayName("전시회_제목이 1자 이상 50자 이하이면 성공한다")
    @ParameterizedTest
    @ValueSource(strings = {"오스틴 리 전시회", "데이비드 호크니 전시회", "기억의 캐비닛 전시회"})
    void 전시회_제목이_1자_이상_50자_이하이면_성공한다(final String validTitle) {
        // given
        Exhibition exhibition = new Exhibition(VALID_LINK, validTitle, VALID_IMAGE, VALID_PLACE, VALID_PRICE);

        // when
        String actual = exhibition.getTitle();

        // then
        assertThat(actual).hasSizeBetween(1, 50);
    }

    @DisplayName("전시회 제목이 50자 이상 초과하면 예외를 던진다")
    @Test
    void 전시회_제목이_50자_이상_초과하면_예외를_던진다() {
        // given
        String longerThanFifty = "a".repeat(51);

        // when & then
        assertThatThrownBy(() -> new Exhibition(VALID_LINK, longerThanFifty, VALID_IMAGE, VALID_PLACE, VALID_PRICE))
                .isInstanceOf(InvalidExhibitionException.class);
    }

    @DisplayName("전시회 제목이 공백이거나 null 이면 예외를 던진다")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void 전시회_제목이_공백이거나_null_이면_예외를_던진다(final String invalidTitle) {
        // given & when & then
        assertThatThrownBy(() -> new Exhibition(VALID_LINK, invalidTitle, VALID_IMAGE, VALID_PLACE, VALID_PRICE))
                .isInstanceOf(InvalidExhibitionException.class);
    }
}
