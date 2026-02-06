package com.hibitbackendimproved.post.exception;

public class InvalidExhibitionException extends RuntimeException {

    private static final String MESSAGE = "전시회 제목은 1자 이상 50자 이하여야 합니다.";

    public InvalidExhibitionException() {
        super(MESSAGE);
    }

    public InvalidExhibitionException(final String message) {
        super(message);
    }
}
