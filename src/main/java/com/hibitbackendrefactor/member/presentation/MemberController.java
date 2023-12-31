package com.hibitbackendrefactor.member.presentation;

import com.hibitbackendrefactor.auth.dto.LoginMember;
import com.hibitbackendrefactor.auth.presentation.AuthenticationPrincipal;
import com.hibitbackendrefactor.member.application.MemberService;
import com.hibitbackendrefactor.member.domain.Member;
import com.hibitbackendrefactor.member.domain.MemberRepository;
import com.hibitbackendrefactor.member.exception.NotFoundMemberException;
import com.hibitbackendrefactor.member.dto.MemberIsProfileResponse;
import com.hibitbackendrefactor.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "members", description = "회원")
@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberController(final MemberService memberService, final MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMe(@AuthenticationPrincipal final LoginMember loginMember) {
        MemberResponse response = memberService.findById(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity<MemberIsProfileResponse> findIdx(@AuthenticationPrincipal final LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(NotFoundMemberException::new);

        MemberIsProfileResponse memberIsProfileResponse = new MemberIsProfileResponse(member);
        return ResponseEntity.ok(memberIsProfileResponse);
    }

}
