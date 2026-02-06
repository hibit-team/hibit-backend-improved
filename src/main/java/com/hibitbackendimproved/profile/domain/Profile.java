package com.hibitbackendimproved.profile.domain;


import com.hibitbackendimproved.common.BaseEntity;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.profile.exception.InvalidProfileException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "profiles")
@Entity
public class Profile extends BaseEntity {

    private static final int MAX_NICK_NAME_LENGTH = 20;
    private static final int MAX_INTRODUCE_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "nickname", length = 20, unique = true)
    private String nickname;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "introduce", length = 200)
    private String introduce;


    protected Profile() {
    }

    @Builder
    public Profile(final Member member, final String nickname, final String profileImage, final String introduce) {
        validateNickName(nickname);
        validateProfileImage(profileImage);
        validateIntroduce(introduce);
        this.member = member;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.introduce = introduce;
    }

    private void validateNickName(final String nickname) {
        if (nickname.isBlank() || nickname.length() > MAX_NICK_NAME_LENGTH) {
            throw new InvalidProfileException(String.format("이름은 1자 이상 1자 %d 이하여야 합니다.", MAX_NICK_NAME_LENGTH));
        }
    }

    private void validateProfileImage(final String profileImage) {
        if (profileImage.isBlank()) {
            throw new InvalidProfileException();
        }
    }

    private void validateIntroduce(final String introduce) {
        if (introduce == null) {
            return;
        }
        if (introduce.length() > MAX_INTRODUCE_LENGTH) {
            throw new InvalidProfileException(String.format("자기소개는 %d자를 초과할 수 없습니다.", MAX_INTRODUCE_LENGTH));
        }
    }

    public void updateNickname(final String nickname) {
        validateNickName(nickname);
        this.nickname = nickname;
    }

    public void updateIntroduce(final String introduce) {
        validateIntroduce(introduce);
        this.introduce = introduce;
    }

    public void updateProfileImage(final String profileImage) {
        validateProfileImage(profileImage);
        this.profileImage = profileImage;
    }
}
