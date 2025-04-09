package com.example.promptsentinel.domain.security.application;


import com.example.promptsentinel.domain.member.dto.MemberRepository;
import com.example.promptsentinel.domain.member.dto.RefreshTokenRepository;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.member.entity.MemberRole;
import com.example.promptsentinel.domain.member.entity.RefreshToken;
import com.example.promptsentinel.domain.security.application.oauth.Oauth2Factory;
import com.example.promptsentinel.domain.security.application.oauth.Oauth2Service;
import com.example.promptsentinel.domain.security.dto.request.RegisterRequest;
import com.example.promptsentinel.domain.security.dto.response.AuthResponse;
import com.example.promptsentinel.domain.security.dto.user.UserInfo;
import com.example.promptsentinel.global.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final Oauth2Factory oauth2Factory;


    @Transactional
    public AuthResponse signIn(RegisterRequest request) {
        Oauth2Service oAuth2Service = oauth2Factory.of(request.getProviderName());
        UserInfo userInfo = oAuth2Service.getUserInfo(request.getCode());
        Member member = findOrSignUp(userInfo);

        return generateResponse(member);
    }

    public AuthResponse generateResponse(Member member) {

        Long memberId = member.getId();
        MemberRole memberRole = member.getRole();

        String accessToken = jwtUtil.generateAccessToken(memberId, memberRole);
        String refreshToken = jwtUtil.generateRefreshToken(memberId);

        refreshTokenRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        token -> {
                            token.updateRefreshToken(refreshToken);
                        },
                        () -> refreshTokenRepository.save(new RefreshToken(memberId, refreshToken))
                );


        Date accessTokenExpiration = jwtUtil.getTokenExpirationDate(accessToken, true);
        Date refreshTokenExpiration = jwtUtil.getTokenExpirationDate(refreshToken, false);


        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpiration)
                .refreshTokenExpiration(refreshTokenExpiration)
                .memberId(memberId)
                .nickName(member.getNickName())
                .profileImage(member.getProfileImage())
                .build();
    }

    private Member findOrSignUp(UserInfo userInfo) {
        return memberRepository.findByProviderId(userInfo.getProviderId())
                .orElseGet(() -> saveMember(userInfo));
    }

    private Member saveMember(UserInfo userInfo) {
        Member member = userInfo.toEntity();
        memberRepository.save(member);
        return memberRepository.save(member);
    }

    @Transactional
    public void withdraw() {
        memberRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }
}
