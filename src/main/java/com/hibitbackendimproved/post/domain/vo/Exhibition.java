package com.hibitbackendimproved.post.domain.vo;

import com.hibitbackendimproved.post.exception.InvalidExhibitionException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Exhibition {

    @Column(name = "exhibition_link", nullable = false)
    private String link;

    @Column(name = "exhibition_title", nullable = false)
    private String title;

    @Column(name = "exhibition_image", nullable = false)
    private String image;

    @Column(name = "exhibition_place")
    private String place;

    @Column(name = "exhibition_price")
    private int price;

    protected Exhibition() {
    }

    public Exhibition(final String link, final String title, final String image, final String place, final int price) {
        validate(link, title, image, place, price);
        this.link = link;
        this.title = title;
        this.image = image;
        this.place = place;
        this.price = price;
    }

    private void validate(final String link, final String title, final String image, final String place, final int price) {
        if (title == null || title.isBlank()) {
            throw new InvalidExhibitionException("전시회 제목은 필수입니다.");
        }

        if (title.length() > 50) {
            throw new InvalidExhibitionException("전시회 제목은 1자 이상 50자 이하여야 합니다.");
        }

        if (price < 0) {
            throw new InvalidExhibitionException("전시회 가격은 0원 이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Exhibition that = (Exhibition) o;
        return price == that.price && Objects.equals(link, that.link) && Objects.equals(title, that.title) && Objects.equals(image, that.image) && Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, title, image, place, price);
    }
}
