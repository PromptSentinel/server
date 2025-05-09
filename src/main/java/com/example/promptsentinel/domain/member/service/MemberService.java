package com.example.promptsentinel.domain.member.service;

import com.example.promptsentinel.domain.member.dao.MemberRepository;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member getMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
