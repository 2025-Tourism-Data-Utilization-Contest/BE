package com.saerok.showing.api.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamJoinRequest {

    @NotNull
    @Schema(description = "가입할 팀 ID입니다.", example = "1")
    private Long teamId;

    @NotNull
    @Size(min = 4, max = 30)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,30}$")
    @Schema(
        description = "팀 입장 시 사용하는 비밀번호입니다. 영문자와 숫자를 조합한 4~30자입니다.",
        example = "team2025"
    )
    private String password;
}
