package com.hibitbackendimproved.auth.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(final String message) {
        super(message);
    }

    public AuthorizationException() {
        this("권한이 없습니다.");
    }
}
