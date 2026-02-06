package com.hibitbackendimproved.member.dto;


import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.SocialType;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String socialId;
    private SocialType socialType;

    private MemberResponse() {
    }

    public MemberResponse(final Long id, final String socialId, final SocialType socialType) {
        this.id = id;
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public MemberResponse(final Member member) {
        this(member.getId(), member.getSocialId(), member.getSocialType());
    }
}
