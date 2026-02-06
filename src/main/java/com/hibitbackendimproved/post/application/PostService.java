package com.hibitbackendimproved.post.application;

import com.hibitbackendimproved.auth.dto.LoginMember;
import com.hibitbackendimproved.auth.exception.AuthorizationException;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.post.domain.Post;
import com.hibitbackendimproved.post.domain.PostRepository;
import com.hibitbackendimproved.post.domain.SearchQuery;
import com.hibitbackendimproved.post.domain.ViewCountManager;
import com.hibitbackendimproved.post.dto.request.PostCreateRequest;
import com.hibitbackendimproved.post.dto.request.PostUpdateServiceRequest;
import com.hibitbackendimproved.post.dto.response.PostDetailResponse;
import com.hibitbackendimproved.post.dto.response.PostsCountResponse;
import com.hibitbackendimproved.post.dto.response.PostsResponse;
import com.hibitbackendimproved.post.dto.response.PostsSliceResponse;
import com.hibitbackendimproved.post.exception.NotFoundPostException;
import com.hibitbackendimproved.profile.domain.Profile;
import com.hibitbackendimproved.profile.domain.ProfileRepository;
import com.hibitbackendimproved.profile.exception.NotFoundProfileException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final ViewCountManager viewCountManager;

    public PostService(final PostRepository postRepository, final MemberRepository memberRepository,
                       final ProfileRepository profileRepository, final ViewCountManager viewCountManager) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.profileRepository = profileRepository;
        this.viewCountManager = viewCountManager;
    }

    @Transactional
    public PostDetailResponse save(final LoginMember loginMember, final PostCreateRequest request) {
        validateMember(loginMember.getId());
        Member foundMember = memberRepository.getByIdOrThrow(loginMember.getId());
        Post savedPost = request.toEntity(foundMember, request);
        postRepository.save(savedPost);
        return PostDetailResponse.of(savedPost, loginMember);
    }

    private void validateMember(final Long memberId) {
        if (!existProfile(memberId)) {
            throw new NotFoundProfileException("프로필을 등록해야 게시글을 저장할 수 있습니다.");
        }
    }

    private boolean existProfile(final Long memberId) {
        Profile profile = profileRepository.findByMemberId(memberId).orElse(null);
        return profile != null;
    }

    public PostsResponse findAll() {
        List<Post> posts = postRepository.findAllByOrderByCreatedDateTimeDesc();
        return PostsResponse.of(posts);
    }

    @Transactional
    public PostDetailResponse findPost(final Long postId, final LoginMember loginMember, final String cookieValue) {
        if (viewCountManager.isFirstAccess(cookieValue, postId)) {
            postRepository.updateViewCount(postId);
        }
        Post foundPost = findPostObject(postId);
        return PostDetailResponse.of(foundPost, loginMember);
    }

    public String updatePostLog(final Long postId, final String cookieValue) {
        return viewCountManager.getUpdatedLog(cookieValue, postId);
    }

    private Post findPostObject(final Long postId) {
        List<Post> posts = postRepository.findPostById(postId);
        if (posts.isEmpty()) {
            throw new NotFoundPostException();
        }
        return posts.get(0);
    }

    public PostsCountResponse countPostWithQuery(final String query) {
        Pageable pageable = PageRequest.of(0, 3, DESC, "created_at");
        SearchQuery searchQuery = new SearchQuery(query);

        Page<Post> posts = postRepository.findPostPagesByQuery(pageable, searchQuery.getValue());
        return PostsCountResponse.of((int) posts.getTotalElements());
    }

    public PostsSliceResponse searchSlickWithQuery(final String query, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), DESC, "created_at");
        SearchQuery searchQuery = new SearchQuery(query);

        Slice<Post> posts = postRepository.findPostSlicePageByQuery(pageable, searchQuery.getValue());
        return PostsSliceResponse.ofPostSlice(posts);
    }

    @Transactional
    public void update(final Long memberId, final Long postId, final PostUpdateServiceRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Post post = findPostObject(postId);
        validateProductMembership(memberId, post);

        post.change(member, request.getTitle(), request.getContent(), request.getExhibition(), request.getExhibitionAttendance(), request.getPossibleTime(), request.getOpenChatUrl(),
                request.getTogetherActivity(), request.getImageName(), request.getPostStatus());
    }

    @Transactional
    public void delete(final Long memberId, final Long postId) {
        Post post = findPostObject(postId);
        validateProductMembership(memberId, post);

        postRepository.delete(post);
    }

    private void validateProductMembership(final Long memberId, final Post post) {
        if (!post.isMember(memberId)) {
            throw new AuthorizationException();
        }
    }
}
