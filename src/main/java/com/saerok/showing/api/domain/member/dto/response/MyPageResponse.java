package com.saerok.showing.api.domain.member.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.route.dto.response.RouteSummaryResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponse {

    private String name;

    private String email;

    private String profileImage;

    private int postCount;

     private List<RouteSummaryResponse> routes;

    public static MyPageResponse toDto(
        Member member,
        int postCount,
        List<RouteSummaryResponse> routes
    ) {
        return MyPageResponse.builder()
            .name(member.getName())
            .email(member.getEmail())
            .profileImage(member.getProfileImage())
            .postCount(postCount)
            .routes(routes)
            .build();
    }
}
