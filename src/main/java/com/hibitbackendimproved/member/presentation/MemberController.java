package com.hibitbackendimproved.member.presentation;

import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.auth.presentation.AuthenticationPrincipal;
import com.hibitbackendimproved.member.application.MemberService;
import com.hibitbackendimproved.member.dto.MemberResponse;
import com.hibitbackendimproved.support.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> findMe(@AuthenticationPrincipal final LoginMember loginMember) {
        MemberResponse response = memberService.findById(loginMember.getId());
        ApiResponse<MemberResponse> apiResponse = ApiResponse.ok(response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
