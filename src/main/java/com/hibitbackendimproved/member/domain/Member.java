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
import lombok.Builder;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Table(name = "members")
@Entity
public class Member extends BaseEntity {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");
    private static final int MAX_DISPLAY_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = true)
    private SocialType socialType;

    protected Member() {
    }

    @Builder
    public Member(final String email, final String nickname, final SocialType socialType) {
        super();
        validateEmail(email);
        validateNickName(nickname);

        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    private void validateEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidMemberException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validateNickName(final String nickname) {
        if (nickname.isBlank() || nickname.length() > MAX_DISPLAY_NAME_LENGTH) {
            throw new InvalidMemberException(String.format("이름은 1자 이상 20자 %d이하여야 합니다.", MAX_DISPLAY_NAME_LENGTH));
        }
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }
}
