package com.example.promptsentinel.domain.security.application;

import com.example.promptsentinel.domain.member.dto.RefreshTokenRepository;
import com.example.promptsentinel.domain.member.entity.MemberRole;
import com.example.promptsentinel.domain.member.entity.RefreshToken;
import com.example.promptsentinel.domain.security.dto.token.AccessTokenDto;
import com.example.promptsentinel.domain.security.dto.token.RefreshTokenDto;
import com.example.promptsentinel.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.promptsentinel.global.common.constants.SecurityConstants.TOKEN_ROLE_NAME;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createAccessToken(Long memberId, MemberRole memberRole) {
        return jwtUtil.generateAccessToken(memberId, memberRole);
    }

    public AccessTokenDto createAccessTokenDto(Long memberId, MemberRole memberRole) {
        return jwtUtil.generateAccessTokenDto(memberId, memberRole);
    }

    public String createRefreshToken(Long memberId) {
        String token = jwtUtil.generateRefreshToken(memberId);
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .memberId(memberId)
                        .token(token)
                        .ttl(jwtUtil.getRefreshTokenExpirationTime())
                        .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public RefreshTokenDto createRefreshTokenDto(Long memberId) {
        RefreshTokenDto refreshTokenDto = jwtUtil.generateRefreshTokenDto(memberId);
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .memberId(memberId)
                        .token(refreshTokenDto.token())
                        .ttl(refreshTokenDto.ttl())
                        .build();
        refreshTokenRepository.save(refreshToken);
        return refreshTokenDto;
    }


    public AccessTokenDto retrieveOrReissueAccessToken(String accessTokenValue) {
        try {
            return jwtUtil.parseAccessToken(accessTokenValue);
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());
            MemberRole memberRole =
                    MemberRole.valueOf(e.getClaims().get(TOKEN_ROLE_NAME, String.class));
            return createAccessTokenDto(memberId, memberRole);
        } catch (Exception e) {
            return null;
        }
    }


    public RefreshTokenDto retrieveRefreshToken(String refreshTokenValue) {
        RefreshTokenDto refreshTokenDto = jwtUtil.parseRefreshToken(refreshTokenValue);

        if (refreshTokenDto == null) {
            return null;
        }

        Optional<RefreshToken> refreshToken = getRefreshTokenFromRedis(refreshTokenDto.memberId());

        if (refreshToken.isPresent()
                && refreshTokenDto.token().equals(refreshToken.get().getRefreshToken())) {
            return refreshTokenDto;
        }

        return null;
    }

    private Optional<RefreshToken> getRefreshTokenFromRedis(Long memberId) {
        return refreshTokenRepository.findById(memberId);
    }
}