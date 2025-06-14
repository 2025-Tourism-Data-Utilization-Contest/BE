package com.saerok.showing.api.global.auth.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.entity.Role;
import com.saerok.showing.api.domain.member.repository.MemberRepository;
import com.saerok.showing.api.global.auth.domain.CustomOAuth2Member;
import com.saerok.showing.api.global.auth.dto.KakaoResponse;
import com.saerok.showing.api.global.auth.dto.NaverResponse;
import com.saerok.showing.api.global.auth.dto.OAuth2MemberDto;
import com.saerok.showing.api.global.auth.dto.OAuth2Response;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Response oAuth2Response = extractOAuth2Response(userRequest, oAuth2User);
        Member member = upsertOAuth2Member(oAuth2Response);
        OAuth2MemberDto oAuth2MemberDto = OAuth2MemberDto.create(member);
        return new CustomOAuth2Member(oAuth2MemberDto, oAuth2User.getAttributes());
    }

    private OAuth2Response extractOAuth2Response(OAuth2UserRequest request, OAuth2User oAuth2User) {
        String provider = request.getClientRegistration().getRegistrationId();
        return switch (provider) {
            case "naver" -> new NaverResponse(oAuth2User.getAttributes());
            case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
            default -> throw ShowingException.from(ErrorCode.UNSUPPORTED_OAUTH2_PROVIDER);
        };
    }

    private Member upsertOAuth2Member(OAuth2Response response) {
        return memberRepository.findByEmail(response.getEmail())
            .map(existing -> updateMember(existing, response.getName()))
            .orElseGet(() -> createMember(response));
    }

    private Member createMember(OAuth2Response oAuth2Response) {
        Member member = memberRepository.save(buildMember(oAuth2Response));
        return member;
    }

    private Member buildMember(OAuth2Response oAuth2Response) {
        return Member.builder()
            .email(oAuth2Response.getEmail())
            .name(oAuth2Response.getName())
            .profileImage(oAuth2Response.getProfileImage())
            .role(Role.USER)
            .build();
    }

    private Member updateMember(Member member, String newName) {
        member.setName(member.getName() == null ? newName : member.getName());
        member.setRole(member.getRole() == null ? Role.USER : member.getRole());
        return member;
    }
}
