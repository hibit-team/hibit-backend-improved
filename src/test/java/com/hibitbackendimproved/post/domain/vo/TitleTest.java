package com.hibitbackendimproved.post.domain.vo;

import com.hibitbackendimproved.post.exception.InvalidTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TitleTest {

    @DisplayName("게시글 제목을 생성한다.")
    @Test
    void 게시글_제목을_생성한다() {
        // given
        String title = "오스틴리 전시회 보러 오실 분 있나요?";

        // when & then
        assertDoesNotThrow(() -> new Title(title));
    }
    @DisplayName("게시글 제목이 1자 이상 30자 이하이면 성공한다")
    @ParameterizedTest
    @ValueSource(strings = {"일", "오스틴리 전시회 놀러오실 분~", "데이비드 호크니 같이 보러 가실분 있나요?",  "앨리스: 인투 더 뉴월드 같이 보러 가실분~", "기억의 캐비닛 전시회 관심 있으신 분 있을까요?"})
    void 게시글_제목이_1자_이상_30자_이하이면_성공한다(String value) {
        // given
        Title title = new Title(value);

        // when
        String actualTitle = title.getValue();

        // then
        assertThat(actualTitle).hasSizeBetween(1, 30);
     }

    @DisplayName("게시글_제목이 30자 이상 초과하면 예외를 던진다")
    @Test
    void 게시글_제목이_30자_이상_초과하면_예외를_던진다() {
        // given
        String LongerThanThirty = "a".repeat(31);

        // when & then
        assertThatThrownBy(() -> new Title(LongerThanThirty))
                .isInstanceOf(InvalidTitleException.class)
                .hasMessageContaining("제목은 1자 이상 30자 이하여야 합니다.");
    }

    @DisplayName("게시글 제목이 공백이거나 null 이면 예외를 던진다")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "      "})
    void 게시글_제목이_공백이거나_null_이면_예외를_던진다(String value) {

        // given & when & then
        assertThatThrownBy(() -> new Title(value))
                .isInstanceOf(InvalidTitleException.class)
                .hasMessageContaining("제목은 1자 이상 30자 이하여야 합니다.");
    }

    @DisplayName("동일한 내용을 가진 Title 객체는 equals에서 true를 반환한다.")
    @Test
    void equals_SameETitle_ReturnsTrue() {
        // given
        Title title1 = new Title("This is some title");
        Title title2 = new Title("This is some title");

        // when & then
        assertThat(title1).isEqualTo(title2);
    }

    @DisplayName("다른 내용을 가진 Title 객체는 equals에서 false를 반환한다.")
    @Test
    void equals_DifferentTitle_ReturnsFalse() {
        // given
        Title title1 = new Title("title 1");
        Title title2 = new Title("title 2");

        // when & then
        assertThat(title1).isNotEqualTo(title2);
    }

    @DisplayName("동일한 내용을 가진 Title 객체들은 hashCode가 동일하다.")
    @Test
    void hashCode_SameTitle_ReturnsEqualHashCode() {
        // given
        Title title1 = new Title("This is some title");
        Title title2 = new Title("This is some title");

        // when & then
        assertThat(title1.hashCode()).isEqualTo(title2.hashCode());
    }

    @DisplayName("다른 내용을 가진 Title 객체들은 hashCode가 다르다.")
    @Test
    void hashCode_DifferentTitle_ReturnsDifferentHashCode() {
        // given
        Title title1 = new Title("title 1");
        Title title2 = new Title("title 2");

        // when & then
        assertThat(title1.hashCode()).isNotEqualTo(title2.hashCode());
    }
}
