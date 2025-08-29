package com.saerok.showing.api.domain.post.dto.request;

import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.domain.post.entity.PostVisibility;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {

    @NotNull
    @Size(max = 100, message = "게시글 제목은 최대 100자까지 입력할 수 있습니다.")
    @Schema(description = "게시글 제목", example = "순천만 습지 다녀온 썰")
    private String title;

    @NotNull
    @Schema(description = "게시글 가시성", example = "TEAM_ONLY")
    private PostVisibility visibility;

    @NotNull
    @Schema(description = "게시글 타입", example = "NORMAL")
    private PostType postType;

    @NotNull
    @Size(max = 1000, message = "게시글 내용은 최대 1000자까지 입력할 수 있습니다.")
    @Schema(description = "게시글 내용", example = "순천만 습지 처음 와봤는데 너무 재밌었어요~!")
    private String content;

    @NotNull
    @Schema(
        description = "게시글 이미지 URL 리스트",
        example = "[\"https://bucket.s3.ap-northeast-2.amazonaws.com/post/sample.png\"]"
    )
    private List<String> imageUrls;

    @NotNull
    @Schema(description = "해시태그 목록", example = "[\"#순천만습지\", \"#두루미\"]")
    private List<String> hashtags;
}
