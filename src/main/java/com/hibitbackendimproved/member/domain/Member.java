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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    @Column(name = "display_name", nullable = false)
    private String displayName; // 구글로부터 가져오는 닉네임

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isProfile;

    @Column(name = "main_image", nullable = true)
    private String mainImage;

    protected Member() {
    }

    @Builder
    public Member(final String email, final String displayName, final SocialType socialType) {
        super();
        validateEmail(email);
        validateDisplayName(displayName);

        this.email = email;
        this.displayName = displayName;
        this.socialType = socialType;
    }

    private void validateEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidMemberException("이메일 형식이 올바르지 않습니다.");
        }
    }

    /**
     * isEmpty() 는 "" 에 대한 문자열을 확인 -> 비어있는 경우 true 반환
     * isBlank() "", " " 에 대한 문자열을 확인 -> 비어있는 경우 true 반환
     */
    private void validateDisplayName(final String displayName) {
        if (displayName.isBlank() || displayName.length() > MAX_DISPLAY_NAME_LENGTH) {
            throw new InvalidMemberException(String.format("이름은 1자 이상 20자 %d이하여야 합니다.", MAX_DISPLAY_NAME_LENGTH));
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void updateDisplayName(final String nickname) {
        this.displayName = nickname;
    }

    public boolean getIsprofile() {
        return isProfile;
    }

    public void updateIsprofile() {
        this.isProfile = true;
    }

    public void updateMainImage(final String mainImage) {
        this.mainImage = mainImage;
    }
}
