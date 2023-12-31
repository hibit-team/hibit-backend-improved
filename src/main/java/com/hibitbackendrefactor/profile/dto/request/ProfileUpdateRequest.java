package com.hibitbackendrefactor.profile.dto.request;

import com.hibitbackendrefactor.profile.domain.AddressCity;
import com.hibitbackendrefactor.profile.domain.AddressDistrict;
import com.hibitbackendrefactor.profile.domain.PersonalityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileUpdateRequest {

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @NotNull(message = "나이를 입력해 주세요.")
    private int age;

    @NotNull(message = "성별을 선택해 주세요.")
    private int gender;

    @NotEmpty(message = "성격을 골라주세요.( 최대 5개)")
    private List<PersonalityType> personality;

    @NotBlank(message = "메이트에게 자신을 소개해 주세요.")
    private String introduce;

    private List<MultipartFile> images;

    private String job;

    private AddressCity addressCity;

    private AddressDistrict addressDistrict;

    private boolean jobVisibility;

    private boolean addressVisibility;

    private boolean myImageVisibility;

    public ProfileUpdateRequest(final String nickname, final int age, final int gender
            , final List<PersonalityType> personality, final String introduce, final List<MultipartFile> images
            , final String job, final AddressCity addressCity, final AddressDistrict addressDistrict
            , final boolean jobVisibility, final boolean addressVisibility, final boolean myImageVisibility) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.introduce = introduce;
        this.images = images;
        this.job = job;
        this.addressCity = addressCity;
        this.addressDistrict = addressDistrict;
        this.jobVisibility = jobVisibility;
        this.addressVisibility = addressVisibility;
        this.myImageVisibility = myImageVisibility;
    }
}
