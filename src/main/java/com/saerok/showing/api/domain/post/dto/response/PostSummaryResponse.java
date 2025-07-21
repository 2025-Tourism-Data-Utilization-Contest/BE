package com.saerok.showing.api.domain.post.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
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
public class PostSummaryResponse {

    private Long id;

    private String title;

    private String writer;

    private String writerProfileImage;

    private String content;

    private List<ExternalFileResponse> postImages;

    private PostType postType;

    private int likeCount;

    private int commentCount;

    public static PostSummaryResponse toDto(Post post, int commentCount) {
        List<UploadedFile> files = post.getPostImages();
        return PostSummaryResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .writer(post.getMember().getName())
            .writerProfileImage(post.getMember().getProfileImage())
            .content(post.getContent())
            .postImages(ExternalFileResponse.toListDto(files))
            .postType(post.getPostType())
            .likeCount(post.getLikeCount())
            .commentCount(commentCount)
            .build();
    }
}
