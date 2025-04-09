package com.example.promptsentinel.domain.security.dto.response;

import com.example.promptsentinel.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long memberId;
    private String nickName;
    private String profileImage;
    private String accessToken;
    private MemberRole memberRole;
    private String refreshToken;
    private Date accessTokenExpiration;
    private Date refreshTokenExpiration;
}