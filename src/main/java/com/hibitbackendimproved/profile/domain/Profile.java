package com.hibitbackendimproved.profile.domain;


import com.hibitbackendimproved.common.BaseEntity;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.profile.exception.InvalidIntroduceException;
import com.hibitbackendimproved.profile.exception.InvalidNicknameException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private static final int PERSONALITY_COUNT = 1;
    private static final int MAX_INTRODUCE_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @Column(name = "nickname", length = 20, unique = true)
    private String nickname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", nullable = false)
    private int gender;

    @Column(name = "personality", nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonalityType personality;

    @Column(name = "introduce", length = 200, nullable = false)
    private String introduce;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "job", length = 50, nullable = false)
    private String job;

    @Column(name = "address_city", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressCity addressCity;

    @Column(name = "address_distinct", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressDistrict addressDistrict;

    @Column(name = "job_visible")
    private boolean jobVisible;

    @Column(name = "address_visible")
    private boolean addressVisible;

    @Column(name = "my_image_visibility")
    private boolean myImageVisibility;

    protected Profile() {
    }

    @Builder
    public Profile(final Member member, final String nickname, final int age
            , final int gender, final PersonalityType personality, final String introduce, final String imageName
            , final String job, final AddressCity addressCity, final AddressDistrict addressDistrict
            , final boolean jobVisible, final boolean addressVisible, final boolean myImageVisibility) {
        validateNickName(nickname);
        validateIntroduce(introduce);
        this.member = member;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.introduce = introduce;
        this.imageName = imageName;
        this.job = job;
        this.addressCity = addressCity;
        this.addressDistrict = addressDistrict;
        this.jobVisible = jobVisible;
        this.addressVisible = addressVisible;
        this.myImageVisibility = myImageVisibility;
    }

    private void validateNickName(final String nickname) {
        if (nickname.isBlank() || nickname.length() > MAX_NICK_NAME_LENGTH) {
            throw new InvalidNicknameException(String.format("이름은 1자 이상 1자 %d 이하여야 합니다.", MAX_NICK_NAME_LENGTH));
        }
    }

    private void validateIntroduce(final String introduce) {
        if (introduce.isBlank()) {
            throw new InvalidIntroduceException();
        }
        if (introduce.length() > MAX_INTRODUCE_LENGTH) {
            throw new InvalidIntroduceException();
        }
    }

    public void updateNickname(final String nickname) {
        validateNickName(nickname);
        this.nickname = nickname;
    }

    public void updateAge(final int age) {
        this.age = age;
    }

    public void updateGender(final int gender) {
        this.gender = gender;
    }

    public void updatePersonality(final PersonalityType personality) {
        this.personality = personality;
    }

    public void updateIntroduce(final String introduce) {
        validateIntroduce(introduce);
        this.introduce = introduce;
    }
    public void updateImageName(final String imageName) {
        this.imageName = imageName;
    }

    public void updateJob(final String job) {
        this.job = job;
    }

    public void updateAddressCity(final AddressCity addressCity) {
        this.addressCity = addressCity;
    }

    public void updateAddressDistinct(final AddressDistrict addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public void updateJobVisible(final boolean jobVisible) {
        this.jobVisible = jobVisible;
    }

    public void updateAddressVisible(final boolean addressVisible) {
        this.addressVisible = addressVisible;
    }

    public void updateMyImageVisibility(final boolean myImageVisibility) {
        this.myImageVisibility = myImageVisibility;
    }
}
