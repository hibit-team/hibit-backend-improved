package com.hibitbackendimproved.profile.domain;

import com.hibitbackendimproved.profile.exception.NotFoundProfileException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    default Profile getByIdOrThrow(final Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundProfileException::new);
    }

    @Query("SELECT p "
            + "FROM Profile p "
            + "WHERE p.member.id = :memberId")
    Optional<Profile> findByMemberId(@Param("memberId") final Long memberId);

    boolean existsByNickname(final String nickname);

    boolean existsByMemberId(final Long memberId);
}
