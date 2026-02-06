package com.hibitbackendimproved.member.domain;

import com.hibitbackendimproved.member.exception.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialId(final String email);

    default Member getByIdOrThrow(final Long id) {
        return findById(id)
                .orElseThrow(NotFoundMemberException::new);
    }

    boolean existsBySocialId(final String email);

    default Member getBySocialIdOrThrow(final String socialId) {
        return findBySocialId(socialId)
                .orElseThrow(NotFoundMemberException::new);
    }

    default void validateExistById(final Long id) {
        if (!existsById(id)) {
            throw new NotFoundMemberException();
        }
    }

}
