package com.saerok.showing.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentCreateRequest {

    @NotNull
    @Schema(description = "댓글", example = "이 사진 정말 예쁘네요~! 어디인가요?")
    private String comment;
}
