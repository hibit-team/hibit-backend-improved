package com.hibitbackendimproved.auth.domain;

import com.hibitbackendimproved.auth.exception.NotFoundTokenException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryAuthTokenRepository implements TokenRepository {

    private static final Map<Long, String> TOKEN_REPOSITORY = new ConcurrentHashMap<>();

    @Override
    public String save(final Long memberId, final String refreshToken) {
        TOKEN_REPOSITORY.put(memberId, refreshToken);
        return TOKEN_REPOSITORY.get(memberId);
    }

    @Override
    public void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }

    @Override
    public boolean exist(final Long memberId) {
        return TOKEN_REPOSITORY.containsKey(memberId);
    }

    @Override
    public String getToken(final Long memberId) {
        Optional<String> token = Optional.ofNullable(TOKEN_REPOSITORY.get(memberId));
        return token.orElseThrow(() -> new NotFoundTokenException("일치하는 토큰이 존재하지 않습니다."));
    }
}
