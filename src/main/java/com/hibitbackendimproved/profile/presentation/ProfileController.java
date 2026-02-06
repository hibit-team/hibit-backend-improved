package com.hibitbackendimproved.profile.presentation;


import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.auth.presentation.AuthenticationPrincipal;
import com.hibitbackendimproved.profile.application.ProfileService;
import com.hibitbackendimproved.profile.dto.request.ProfileCreateRequest;
import com.hibitbackendimproved.profile.dto.request.ProfileUpdateRequest;
import com.hibitbackendimproved.profile.dto.response.ProfileOtherResponse;
import com.hibitbackendimproved.profile.dto.response.ProfileResponse;
import com.hibitbackendimproved.support.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/api/v1/profile/new")
    public ResponseEntity<ApiResponse<ProfileResponse>> saveMyProfile(@AuthenticationPrincipal final LoginMember loginMember,
                                                                      @Valid @RequestBody final ProfileCreateRequest request) {
        ProfileResponse profileResponse = profileService.save(loginMember.getId(), request);
        ApiResponse<ProfileResponse> apiResponse = ApiResponse.created(profileResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/profile/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> findMyProfile(@AuthenticationPrincipal final LoginMember loginMember) {
        ProfileResponse profileResponse = profileService.findMyProfile(loginMember.getId());
        ApiResponse<ProfileResponse> apiResponse = ApiResponse.ok(profileResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/api/v1/profile/other/{id}")
    public ResponseEntity<ApiResponse<ProfileOtherResponse>> findOtherProfile(@AuthenticationPrincipal final LoginMember loginMember,
                                                                              @PathVariable(name = "id") final Long otherMemberId) {
        ProfileOtherResponse profileOtherResponse = profileService.findOtherProfile(otherMemberId);
        ApiResponse<ProfileOtherResponse> apiResponse = ApiResponse.ok(profileOtherResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/api/v1/profile/me")
    public ResponseEntity<ApiResponse<Void>> update(@AuthenticationPrincipal final LoginMember loginMember,
                                                    @Valid @RequestBody final ProfileUpdateRequest request) {
        profileService.update(loginMember.getId(), request);
        ApiResponse<Void> apiResponse = ApiResponse.noContent();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
}
