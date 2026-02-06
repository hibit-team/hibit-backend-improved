package com.hibitbackendimproved.post.domain;

import com.hibitbackendimproved.common.BaseEntity;
import com.hibitbackendimproved.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "posts")
@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @Column(name = "title", nullable = false)
    @Embedded
    private Title title;

    @Column(name = "content", nullable = false)
    @Embedded
    private Content content;

    @Column(name = "exhibition", nullable = false)
    @Embedded
    private Exhibition exhibition;

    @Column(name = "exhibition_image", nullable = false)
    private String exhibitionImage;

    @Column(name = "open_chat_url", nullable = false)
    private String openChatUrl;

    @Column(name = "post_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    private int viewCount = 0;

    protected Post() {
    }

    @Builder
    public Post(final Member member, final String title, final String content
            , final String exhibition, final String exhibitionImage
            , final String openChatUrl, final PostStatus postStatus) {
        this.member = member;
        this.title = new Title(title);
        this.content = new Content(content);
        this.exhibition = new Exhibition(exhibition);
        this.exhibitionImage = exhibitionImage;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
    }

    public void change(final Member member, final String title, final String content
            , final String exhibition, final String exhibitionImage
            , final String openChatUrl, final PostStatus postStatus) {
        this.member = member;
        this.title = new Title(title);
        this.content = new Content(content);
        this.exhibition = new Exhibition(exhibition);
        this.exhibitionImage = exhibitionImage;
        this.openChatUrl = openChatUrl;
        this.postStatus = postStatus;
    }

    public boolean isMember(final Long accessMemberId) {
        if (accessMemberId == null) {
            return false;
        }
        return member.getId().equals(accessMemberId);
    }
}
