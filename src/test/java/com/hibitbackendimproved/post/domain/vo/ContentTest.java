package com.hibitbackendimproved.post.domain.vo;

import com.hibitbackendimproved.post.exception.InvalidContentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ContentTest {

    @DisplayName("게시글 내용을 생성한다.")
    @Test
    void 게시글_내용을_생성한다() {
        // given
        String content = "오스틴 리 전시회 보러가실 분";

        // when & then
        assertDoesNotThrow(() -> new Content(content));
    }

    @DisplayName("내용이 1자 이상 200자 이하이면 성공한다")
    @ParameterizedTest
    @ValueSource(strings = {"오스틴 리 전시회 보러가실 분", "라이트룸서울에서 진행하는 데이비드 호크니 전시회 보러가실 분 있을까요?",
            "기억의 캐비닛 전시회 보고 시간 괜찮으시다면, 카페에서 얘기 나누실 분 있을까요?"})
    void 내용이_1자_이상_200자_이하이면_성공한다(String value) {
        // given
        Content content = new Content(value);

        // when
        String actual = content.getValue();

        // then
        assertThat(actual).hasSizeBetween(1, 200);
    }

    @DisplayName("내용이 200자 이상 초과하면 예외를 던진다")
    @Test
    void 내용이_200자_이상_초과하면_예외를_던진다() {
        // given
        String longerThanTwoHundred = "a".repeat(201);

        // when & then
        assertThatThrownBy(() -> new Content(longerThanTwoHundred))
                .isInstanceOf(InvalidContentException.class)
                .hasMessageContaining("본문은 1자 이상 200자 이하여야 합니다.");
    }



    @DisplayName("내용이 공백이거나 null 이면 예외를 던진다")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void 내용이_공백이거나_null_이면_예외를_던진다(String value) {

        // given & when & then
        assertThatThrownBy(() -> new Content(value))
                .isInstanceOf(InvalidContentException.class)
                .hasMessageContaining("본문은 1자 이상 200자 이하여야 합니다.");
    }

    @DisplayName("동일한 내용을 가진 Content 객체는 equals에서 true를 반환한다.")
    @Test
    void equals_SameContent_ReturnsTrue() {
        // given
        Content content1 = new Content("This is some content");
        Content content2 = new Content("This is some content");

        // when & then
        assertThat(content1).isEqualTo(content2);
    }

    @DisplayName("다른 내용을 가진 Content 객체는 equals에서 false를 반환한다.")
    @Test
    void equals_DifferentContent_ReturnsFalse() {
        // given
        Content content1 = new Content("Content 1");
        Content content2 = new Content("Content 2");

        // when & then
        assertThat(content1).isNotEqualTo(content2);
    }

    @DisplayName("동일한 내용을 가진 Content 객체들은 hashCode가 동일하다.")
    @Test
    void hashCode_SameContent_ReturnsEqualHashCode() {
        // given
        Content content1 = new Content("This is some content");
        Content content2 = new Content("This is some content");

        // when & then
        assertThat(content1.hashCode()).isEqualTo(content2.hashCode());
    }

    @DisplayName("다른 내용을 가진 Content 객체들은 hashCode가 다르다.")
    @Test
    void hashCode_DifferentContent_ReturnsDifferentHashCode() {
        // given
        Content content1 = new Content("Content 1");
        Content content2 = new Content("Content 2");

        // when & then
        assertThat(content1.hashCode()).isNotEqualTo(content2.hashCode());
    }
}
