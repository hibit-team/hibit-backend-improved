package com.hibitbackendimproved.post.domain;

import com.hibitbackendimproved.post.exception.InvalidExhibitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExhibitionTest {

    @DisplayName("전시회 제목을 생성한다.")
    @Test
    void 전시회_제목을_생성한다() {
        // given
        String title = "오스틴리 전시회";

        // when & then
        assertDoesNotThrow(() -> new Exhibition(title));
     }

    @DisplayName("전시회_제목이 1자 이상 50자 이하이면 성공한다")
    @ParameterizedTest
    @ValueSource(strings = {"오스틴 리 전시회", "데이비드 호크니 전시회", "기억의 캐비닛 전시회"})
    void 전시회_제목이_1자_이상_50자_이하이면_성공한다(String value) {
        // given
        Exhibition exhibition = new Exhibition(value);

        // when
        String actual = exhibition.getValue();

        // then
        assertThat(actual).hasSizeBetween(1, 30);
    }

    @DisplayName("전시회 제목이 50자 이상 초과하면 예외를 던진다")
    @Test
    void 전시회_제목이_50자_이상_초과하면_예외를_던진다() {
        // given
        String longerThanFifty = "a".repeat(51);

        // when & then
        assertThatThrownBy(() -> new Exhibition(longerThanFifty))
                .isInstanceOf(InvalidExhibitionException.class)
                .hasMessageContaining("전시회 제목은 1자 이상 50자 이하여야 합니다");
    }

    @DisplayName("전시회 제목이 공백이거나 null 이면 예외를 던진다")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void 전시회_제목이_공백이거나_null_이면_예외를_던진다(String value) {

        // given & when & then
        assertThatThrownBy(() -> new Exhibition(value))
                .isInstanceOf(InvalidExhibitionException.class)
                .hasMessageContaining("전시회 제목은 1자 이상 50자 이하여야 합니다");
    }

    @DisplayName("동일한 내용을 가진 Exhibition 객체는 equals에서 true를 반환한다.")
    @Test
    void equals_SameExhibition_ReturnsTrue() {
        // given
        Exhibition exhibition1 = new Exhibition("This is some exhibition");
        Exhibition exhibition2 = new Exhibition("This is some exhibition");

        // when & then
        assertThat(exhibition1).isEqualTo(exhibition2);
    }

    @DisplayName("다른 내용을 가진 Exhibition 객체는 equals에서 false를 반환한다.")
    @Test
    void equals_DifferentExhibition_ReturnsFalse() {
        // given
        Exhibition exhibition1 = new Exhibition("exhibition 1");
        Exhibition exhibition2 = new Exhibition("exhibition 2");

        // when & then
        assertThat(exhibition1).isNotEqualTo(exhibition2);
    }

    @DisplayName("동일한 내용을 가진 Exhibition 객체들은 hashCode가 동일하다.")
    @Test
    void hashCode_SameExhibition_ReturnsEqualHashCode() {
        // given
        Exhibition exhibition1 = new Exhibition("This is some exhibition");
        Exhibition exhibition2 = new Exhibition("This is some exhibition");

        // when & then
        assertThat(exhibition1.hashCode()).isEqualTo(exhibition2.hashCode());
    }

    @DisplayName("다른 내용을 가진 Exhibition 객체들은 hashCode가 다르다.")
    @Test
    void hashCode_DifferentExhibition_ReturnsDifferentHashCode() {
        // given
        Exhibition exhibition1 = new Exhibition("exhibition 1");
        Exhibition exhibition2 = new Exhibition("exhibition 2");

        // when & then
        assertThat(exhibition1.hashCode()).isNotEqualTo(exhibition2.hashCode());
    }
}
