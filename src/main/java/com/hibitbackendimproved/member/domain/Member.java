package com.hibitbackendimproved.member.domain;

import com.hibitbackendimproved.common.BaseEntity;
import com.hibitbackendimproved.member.exception.InvalidMemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(name = "uk_member_social_info", columnNames = {"social_id", "social_type"})
})
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    protected Member() {
    }

    @Builder
    public Member(final String socialId, final SocialType socialType) {
        validateSocialId(socialId);
        this.socialId = socialId;
        this.socialType = socialType;
    }

    private void validateSocialId(final String socialId) {
        if (socialId == null || socialId.isBlank()) {
            throw new InvalidMemberException("소셜 ID 값이 존재해야 합니다.");
        }
    }
}
