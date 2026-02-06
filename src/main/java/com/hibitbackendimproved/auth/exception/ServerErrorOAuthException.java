package com.hibitbackendimproved.auth.exception;

public class ServerErrorOAuthException extends RuntimeException {

    public ServerErrorOAuthException(final String message) {
        super(message);
    }

    public ServerErrorOAuthException() {
        this("존재하지 않는 OAuthToken 입니다.");
    }
}
