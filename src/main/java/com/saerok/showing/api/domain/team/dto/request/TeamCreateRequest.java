package com.saerok.showing.api.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamCreateRequest {

    @NotNull
    @Size(min = 2, max = 30)
    @Schema(
        description = "팀(그룹, 소속, 학교, 단체) 이름을 입력합니다, 팀 이름은 2 ~ 30글자 입니다.",
        example = "쇼윙"
    )
    private String name;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Z\\d]{6}$")
    @Schema(
        description = "팀 인증코드입니다. 영문 대문자와 숫자를 조합한 정확히 6자리 문자열이어야 합니다.",
        example = "SHOW25"
    )
    private String password;
}
