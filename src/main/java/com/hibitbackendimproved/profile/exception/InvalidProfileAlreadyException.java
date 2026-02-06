package com.hibitbackendimproved.profile.exception;


public class InvalidProfileAlreadyException extends RuntimeException {
    public InvalidProfileAlreadyException(final String message) {
        super(message);
    }

    public InvalidProfileAlreadyException() {
        this("프로필이 이미 존재합니다.");
    }
}
