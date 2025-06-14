package com.saerok.showing.api.domain.member.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.repository.MemberRepository;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> ShowingException.from(ErrorCode.MEMBER_NOT_FOUND));
    }
}
