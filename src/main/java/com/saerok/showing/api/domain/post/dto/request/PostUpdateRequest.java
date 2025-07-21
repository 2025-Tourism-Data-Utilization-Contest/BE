package com.saerok.showing.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {

    @NotNull
    @Size(max = 100, message = "게시글 제목은 최대 100자까지 입력할 수 있습니다.")
    @Schema(description = "게시글 제목", example = "순천만 습지를 다녀왔더니..")
    private String title;

    @NotNull
    @Size(max = 1000, message = "게시글 내용은 최대 1000자까지 입력할 수 있습니다.")
    @Schema(description = "게시글 내용", example = "순천만 습지 처음 와봤는데 너무 재밌었어요~!!!!!!")
    private String content;
}
