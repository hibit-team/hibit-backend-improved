package com.hibitbackendimproved.post.domain;

import com.hibitbackendimproved.post.exception.InvalidExhibitionException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Exhibition {
    private static final int MAX_EXHIBITION_LENGTH = 50;

    @Column(name = "exhibition", nullable = false)
    @Lob
    private String value;

    protected Exhibition() {
    }

    public Exhibition(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidExhibitionException();
        }
        if (value.length() > MAX_EXHIBITION_LENGTH) {
            throw new InvalidExhibitionException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exhibition)) {
            return false;
        }
        Exhibition exhibition = (Exhibition) o;
        return Objects.equals(value, exhibition.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
