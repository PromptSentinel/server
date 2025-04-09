package com.example.promptsentinel.domain.security.dto.user;

import com.example.promptsentinel.domain.member.entity.Member;

public interface UserInfo {
    String getProviderId();
    Member toEntity();
}
