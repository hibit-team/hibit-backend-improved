package com.hibitbackendimproved.profile.exception;

public class NotFoundAddressCityException extends RuntimeException {
    public NotFoundAddressCityException(final String message) {
        super(message);
    }

    public NotFoundAddressCityException() {
        this("존재하지 않는 도시입니다.");
    }
}
