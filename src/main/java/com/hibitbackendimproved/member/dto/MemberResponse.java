package com.hibitbackendimproved.member.dto;


import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String displayName;
    private SocialType socialType;

    private MemberResponse() {
    }

    public MemberResponse(final Long id, final String email, final String displayName, final SocialType socialType) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.socialType = socialType;
    }

    public MemberResponse(final Member member) {
        this(member.getId(), member.getEmail(), member.getDisplayName(), member.getSocialType());
    }
}
