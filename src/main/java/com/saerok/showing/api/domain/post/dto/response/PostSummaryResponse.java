package com.saerok.showing.api.domain.post.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSummaryResponse {

    private Long id;

    private String title;

    private String writer;

    private String writerProfileImage;

    private PostType postType;

    private LocalDateTime createdAt;

    public static PostSummaryResponse toDto(Post post) {
        return PostSummaryResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .writer(post.getMember().getName())
            .writerProfileImage(post.getMember().getProfileImage())
            .postType(post.getPostType())
            .createdAt(post.getCreatedAt())
            .build();
    }
}
