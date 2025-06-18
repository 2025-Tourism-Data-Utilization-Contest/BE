package com.saerok.showing.api.domain.member.dto.request;

import com.saerok.showing.api.domain.member.entity.LoginType;
import com.saerok.showing.api.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequest {

    @NotNull
    @Schema(description = "이메일", example = "example@showing.com")
    private String email;

    @NotNull
    @Schema(description = "이름", example = "김민상")
    private String name;

    @NotNull
    @Schema(description = "닉네임", example = "민상")
    private String nickname;

    @NotNull
    @Schema(description = "프로필 이미지 URL", example = "http://showing/image.png")
    private String profileImage;

    @NotNull
    @Schema(description = "로그인 타입", example = "LOCAL")
    private LoginType loginType;

    public Member toEntity() {
        return Member.of(email, name, nickname, profileImage, loginType);
    }
}