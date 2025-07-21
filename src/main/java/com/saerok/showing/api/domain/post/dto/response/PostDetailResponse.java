package com.saerok.showing.api.domain.post.dto.response;

import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.global.file.dto.ExternalFileResponse;
import com.saerok.showing.api.global.file.entity.UploadedFile;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponse {

    private Long id;

    private String writer;

    private String writerProfileImage;

    private String title;

    private String content;

    private List<ExternalFileResponse> postImages;

    private PostType postType;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostDetailResponse toDto(Post post, int commentCount) {
        List<UploadedFile> files = post.getPostImages();
        return PostDetailResponse.builder()
            .id(post.getId())
            .writer(post.getMember().getName())
            .writerProfileImage(post.getMember().getProfileImage())
            .title(post.getTitle())
            .content(post.getContent())
            .postImages(ExternalFileResponse.toListDto(files))
            .postType(post.getPostType())
            .likeCount(post.getLikeCount())
            .commentCount(commentCount)
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
    }
}
