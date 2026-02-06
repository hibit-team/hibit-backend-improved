package com.hibitbackendimproved.post.dto.response;

import com.hibitbackendimproved.post.domain.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponse {
    private final List<PostResponse> posts;

    public PostsResponse(final List<PostResponse> posts) {
        this.posts = posts;
    }

    public static PostsResponse of(final List<Post> posts) {
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
        return new PostsResponse(postResponses);
    }
}
