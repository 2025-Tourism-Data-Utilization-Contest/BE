package com.saerok.showing.api.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamCreateRequest {

    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "팀(그룹, 소속, 학교, 단체) 이름을 입력합니다, 팀 이름은 2 ~ 30글자 입니다.", example = "금화초등학교")
    private String name;
}
