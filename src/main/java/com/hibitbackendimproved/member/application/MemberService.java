package com.hibitbackendimproved.member.application;


import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(memberRepository.getByIdOrThrow(id));
    }
}
