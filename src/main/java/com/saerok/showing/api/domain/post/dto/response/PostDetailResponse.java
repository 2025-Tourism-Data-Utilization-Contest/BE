package com.saerok.showing.api.domain.post.dto.response;

import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponse {

    private Long id;

    private String writer;

    private String title;

    private String comment;

    private PostType postType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostDetailResponse toDto(Post post) {
        return PostDetailResponse.builder()
            .id(post.getId())
            .writer(post.getMember().getName())
            .title(post.getTitle())
            .comment(post.getComment())
            .postType(post.getPostType())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
    }
}
