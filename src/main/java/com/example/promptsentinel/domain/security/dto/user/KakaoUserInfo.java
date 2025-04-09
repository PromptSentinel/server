package com.example.promptsentinel.domain.security.dto.user;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.member.entity.MemberRole;
import com.example.promptsentinel.domain.member.entity.Provider;
import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfo implements UserInfo {

    private String id;
    private Map<String, Object> kakao_account;

    @Override
    public Member toEntity() {
        return Member.builder()
                .provider(Provider.KAKAO)
                .nickName(getNickName())
                .profileImage(getProfileImage())
                .providerId(getProviderId())
                .email(getEmail())
                .role(MemberRole.USER)
                .build();
    }

    @Override
    public String getProviderId() {
        return id;
    }

    public String getEmail() {
        return kakao_account.get("email") == null ? null : kakao_account.get("email").toString();
    }

    public String getNickName() {
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        return profile != null ? profile.get("nickname").toString() : null;
    }

    public String getProfileImage() {
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        return profile != null ? profile.get("profile_image_url").toString() : null;
    }

}

