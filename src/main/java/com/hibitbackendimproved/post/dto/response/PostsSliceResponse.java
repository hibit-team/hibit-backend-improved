package com.hibitbackendimproved.post.dto.response;

import com.hibitbackendimproved.post.domain.Post;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsSliceResponse {
    private final List<PostResponse> posts;
    private final boolean lastPage;

    public PostsSliceResponse(final List<PostResponse> posts, final boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }

    public static PostsSliceResponse ofPostSlice(final Slice<Post> postSlice) {
        List<PostResponse> postResponses = postSlice.getContent()
                .stream()
                .map(post -> PostResponse.from(post))
                .collect(Collectors.toList());
        return new PostsSliceResponse(postResponses, postSlice.isLast());
    }
}
