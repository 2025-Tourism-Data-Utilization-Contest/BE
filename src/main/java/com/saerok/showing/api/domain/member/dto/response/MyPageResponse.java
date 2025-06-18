package com.saerok.showing.api.domain.member.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponse {

    private String name;

    private String profileImage;

    // TODO: 내 루트 가져오는 dto 구현 후 구현
    // private List<RouteSummaryResponse> routes;

    public static MyPageResponse toDto(
        Member member
        // List<RouteSummaryResponse> routes
    ) {
        return MyPageResponse.builder()
            .name(member.getName())
            .profileImage(member.getProfileImage())
            //.routes(routes)
            .build();
    }
}
