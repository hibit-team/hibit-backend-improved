package com.hibitbackendimproved.profile.dto.request;

import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.profile.domain.AddressCity;
import com.hibitbackendimproved.profile.domain.AddressDistrict;
import com.hibitbackendimproved.profile.domain.PersonalityType;
import com.hibitbackendimproved.profile.domain.Profile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileCreateRequest {

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @NotNull(message = "나이를 입력해 주세요.")
    private int age;

    @NotNull(message = "성별을 선택해 주세요.")
    private int gender;

    @NotNull(message = "성격을 골라주세요.(1개)")
    private PersonalityType personality;

    @NotBlank(message = "메이트에게 자신을 소개해 주세요.")
    private String introduce;

    private String imageName;

    private String job;

    private AddressCity addressCity;

    private AddressDistrict addressDistrict;

    private boolean jobVisibility;

    private boolean addressVisibility;

    private boolean myImageVisibility;


    @Builder
    public ProfileCreateRequest(final String nickname, final int age, final int gender
            , final PersonalityType personality, final String introduce, final String imageName
            , final String job, final AddressCity addressCity, final AddressDistrict addressDistrict
            , final boolean jobVisibility, final boolean addressVisibility, final boolean myImageVisibility) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.introduce = introduce;
        this.imageName = imageName;
        this.job = job;
        this.addressCity = addressCity;
        this.addressDistrict = addressDistrict;
        this.jobVisibility = jobVisibility;
        this.addressVisibility = addressVisibility;
        this.myImageVisibility = myImageVisibility;
    }

    public Profile toEntity(final Member foundMember, final ProfileCreateRequest newRequest) {
        return Profile.builder()
                .member(foundMember)
                .nickname(newRequest.getNickname())
                .age(newRequest.getAge())
                .gender(newRequest.getGender())
                .personality(newRequest.getPersonality())
                .introduce(newRequest.getIntroduce())
                .job(newRequest.getJob())
                .addressCity(newRequest.getAddressCity())
                .addressDistrict(newRequest.getAddressDistrict())
                .jobVisible(newRequest.isJobVisibility())
                .addressVisible(newRequest.isAddressVisibility())
                .myImageVisibility(newRequest.isMyImageVisibility())
                .build();

    }
}
