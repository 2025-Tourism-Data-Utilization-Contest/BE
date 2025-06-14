package com.saerok.showing.api.global.auth.util;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.global.auth.dto.CustomMemberDetails;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginMemberProvider {

    public Member getCurrentLoginMember() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof CustomMemberDetails customMemberDetails) {
            return customMemberDetails.member();
        }
        throw ShowingException.from(ErrorCode.MEMBER_NOT_FOUND);
    }

    public Long getCurrentLoginMemberId() {
        return getCurrentLoginMember().getId();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw ShowingException.from(ErrorCode.UNAUTHORIZED);
        }
        return authentication;
    }
}
