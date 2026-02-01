package com.hibitbackendimproved.post.presentation;

import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.auth.presentation.AuthenticationPrincipal;
import com.hibitbackendimproved.support.ApiResponse;
import com.hibitbackendimproved.post.application.PostService;
import com.hibitbackendimproved.post.dto.request.PostCreateRequest;
import com.hibitbackendimproved.post.dto.request.PostUpdateRequest;
import com.hibitbackendimproved.post.dto.response.PostDetailResponse;
import com.hibitbackendimproved.post.dto.response.PostsCountResponse;
import com.hibitbackendimproved.post.dto.response.PostsResponse;
import com.hibitbackendimproved.post.dto.response.PostsSliceResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path = "/api/posts/new")
    public ResponseEntity<ApiResponse<PostDetailResponse>> save(@AuthenticationPrincipal final LoginMember loginMember
            , @Valid @RequestBody final PostCreateRequest request) {
        PostDetailResponse response = postService.save(loginMember, request);
        ApiResponse<PostDetailResponse> apiResponse = ApiResponse.created(response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/posts")
    public ResponseEntity<ApiResponse<PostsResponse>> findAll() {
        PostsResponse responses = postService.findAll();
        ApiResponse<PostsResponse> apiResponse = ApiResponse.ok(responses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/api/posts/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> findPost(@PathVariable(name = "id") final Long postId,
                                                                    @AuthenticationPrincipal final LoginMember loginMember,
                                                                    @CookieValue(value = "viewedPost", required = false, defaultValue = "") final String postLog) {
        PostDetailResponse response = postService.findPost(postId, loginMember, postLog);
        String updatedLog = postService.updatePostLog(postId, postLog);
        ResponseCookie responseCookie = ResponseCookie.from("viewedPost", updatedLog).maxAge(86400L).build();

        ApiResponse<PostDetailResponse> apiResponse = ApiResponse.ok(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(apiResponse);
    }

    @GetMapping(path = "/api/posts/count")
    public ResponseEntity<ApiResponse<PostsCountResponse>> searchPostCount(@RequestParam @Nullable String query) {
        PostsCountResponse postsCountResponse = postService.countPostWithQuery(query);
        ApiResponse<PostsCountResponse> apiResponse = ApiResponse.ok(postsCountResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/api/posts/search")
    public ResponseEntity<ApiResponse<PostsSliceResponse>> searchSlicePosts(@RequestParam @Nullable String query,
                                                                            @PageableDefault(sort = "created_date_time", direction = DESC) Pageable pageable) {
        PostsSliceResponse postsSliceResponse = postService.searchSlickWithQuery(query, pageable);
        ApiResponse<PostsSliceResponse> apiResponse = ApiResponse.ok(postsSliceResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping(path = "/api/posts/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> update(@AuthenticationPrincipal final LoginMember loginMember,
                                                                  @PathVariable(name = "id") final Long postId,
                                                                  @Valid @RequestBody final PostUpdateRequest request) {
        postService.update(loginMember.getId(), postId, request.toServiceRequest());
        ApiResponse<PostDetailResponse> apiResponse = ApiResponse.noContent();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/api/posts/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal final LoginMember loginMember,
                                                    @PathVariable(name = "id") final Long postId) {
        postService.delete(loginMember.getId(), postId);
        ApiResponse<Void> apiResponse = ApiResponse.noContent();
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
}
