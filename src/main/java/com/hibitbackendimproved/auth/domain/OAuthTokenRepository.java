package com.hibitbackendimproved.auth.domain;

import com.hibitbackendimproved.auth.exception.NotFoundOAuthTokenException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    boolean existsByMemberId(final Long memberId);

    @Query("SELECT o "
            + "FROM OAuthToken o "
            + "WHERE o.member.id = :memberId")
    Optional<OAuthToken> findByMemberId(@Param("memberId") Long memberId);

    default OAuthToken getByMemberId(final Long memberId) {
        return findByMemberId(memberId)
                .orElseThrow(NotFoundOAuthTokenException::new);
    }

    void deleteAllByMemberId(final Long memberId);
}
