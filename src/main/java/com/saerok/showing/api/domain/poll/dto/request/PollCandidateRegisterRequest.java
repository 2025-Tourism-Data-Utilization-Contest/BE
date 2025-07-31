package com.saerok.showing.api.domain.poll.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PollCandidateRegisterRequest {

    @NotNull
    @Schema(description = "후보로 등록할 Route ID", example = "1")
    private Long routeId;
}
