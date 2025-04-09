package com.example.promptsentinel.domain.security.dto.token;


import com.example.promptsentinel.domain.member.entity.MemberRole;

public record AccessTokenDto(Long memberId, MemberRole memberRole, String token) {}
