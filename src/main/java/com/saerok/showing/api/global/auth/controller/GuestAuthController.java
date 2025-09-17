package com.saerok.showing.api.global.auth.controller;

import com.saerok.showing.api.global.auth.dto.GuestLoginResponse;
import com.saerok.showing.api.global.auth.service.GuestAuthService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Guest Auth", description = "게스트 인증 관리")
public class GuestAuthController {

    private final GuestAuthService guestAuthService;

    @Operation(
        summary = "게스트 로그인",
        description = """
            게스트 계정으로 로그인합니다.<br>
            자동으로 guest{숫자} 형태의 계정이 생성되며, Access Token과 Refresh Token이 발급됩니다.<br>
            기존의 소셜로그인과 로직은 동일하므로 별도의 구현 없이 엔드포인트만 호출하시면 됩니다.
            """
    )
    @PostMapping("/guest")
    public ApiResponse<GuestLoginResponse> guestLogin(HttpServletResponse response) {
        GuestLoginResponse guestLoginResponse = guestAuthService.createGuestLogin(response);
        return ApiResponse.success(guestLoginResponse);
    }
}