package com.saerok.showing.api.domain.poll.dto.request;

import com.saerok.showing.api.domain.poll.entity.PollStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollUpdateRequest {

    @NotNull
    @Size(max = 50, message = "투표 제목은 최대 50자까지 입력할 수 있습니다.")
    @Schema(description = "투표 제목", example = "9/16일 현장체험학습으로 어디를 가면 좋을까요?")
    private String title;

    @NotNull
    @Schema(description = "투표 타입 (예: READY, ONGOING, CLOSED", example = "CLOSED")
    private PollStatus pollStatus;

    @NotNull
    @Schema(description = "투표 시작일 (YYYY-MM-DD)", example = "2025-09-16")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "투표 종료일 (YYYY-MM-DD)", example = "2025-09-19")
    private LocalDate endDate;
}
