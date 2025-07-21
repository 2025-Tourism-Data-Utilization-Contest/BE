package com.saerok.showing.api.domain.post.entity;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.dto.request.PostCreateRequest;
import com.saerok.showing.api.domain.post.dto.request.PostUpdateRequest;
import com.saerok.showing.api.global.entity.BaseEntity;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.file.entity.UploadedFile;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<UploadedFile> postImages;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_hashtag", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> hashtags = new ArrayList<>();

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    public static Post toEntity(Member member, PostCreateRequest request, List<UploadedFile> files) {
        return Post.builder()
            .member(member)
            .title(request.getTitle())
            .postType(request.getPostType())
            .content(request.getContent())
            .postImages(files)
            .hashtags(request.getHashtags())
            .likeCount(0)
            .build();
    }

    public void validateOwner(Member currentMember) {
        if (!this.member.getId().equals(currentMember.getId())) {
            throw ShowingException.from(ErrorCode.POST_WRITER_MISMATCH);
        }
    }

    public void update(PostUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
