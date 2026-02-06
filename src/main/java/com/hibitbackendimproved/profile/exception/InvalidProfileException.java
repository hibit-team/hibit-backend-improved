package com.hibitbackendimproved.profile.exception;

public class InvalidProfileException extends RuntimeException {

    public InvalidProfileException(final String message) {
        super(message);
    }

    public InvalidProfileException() {
        this("프로필 정보에 잘못 입력되었습니다.");
    }
}
