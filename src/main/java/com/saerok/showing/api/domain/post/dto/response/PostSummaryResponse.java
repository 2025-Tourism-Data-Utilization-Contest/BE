package com.saerok.showing.api.domain.post.dto.response;

import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.global.file.dto.ExternalFileResponse;
import com.saerok.showing.api.global.file.entity.UploadedFile;
import com.saerok.showing.api.global.pagination.provider.CreatedAtProvider;
import com.saerok.showing.api.global.pagination.provider.PopularProvider;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSummaryResponse implements CreatedAtProvider, PopularProvider {

    private Long id;

    private String title;

    private String writer;

    private String writerProfileImage;

    private String content;

    private ExternalFileResponse postImage;

    private PostType postType;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    @Override
    public int getLikeCount() {
        return likeCount;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static PostSummaryResponse toDto(Post post, int commentCount) {
        Optional<UploadedFile> firstFile = post.getPostImages().stream().findFirst();
        ExternalFileResponse imageDto = firstFile
            .map(ExternalFileResponse::toDto)
            .orElse(null);

        return PostSummaryResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .writer(post.getMember().getName())
            .writerProfileImage(post.getMember().getProfileImage())
            .content(post.getContent())
            .postImage(imageDto)
            .postType(post.getPostType())
            .likeCount(post.getLikeCount())
            .commentCount(commentCount)
            .createdAt(post.getCreatedAt())
            .build();
    }
}
