package com.example.promptsentinel.domain.security.dto.token;

public record RefreshTokenDto(Long memberId, String token, Long ttl) {
}