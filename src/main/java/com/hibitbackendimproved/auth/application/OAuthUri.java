package com.hibitbackendimproved.auth.application;



public interface OAuthUri {
    String generate(final String redirectUri);
    String getProviderName();
}
