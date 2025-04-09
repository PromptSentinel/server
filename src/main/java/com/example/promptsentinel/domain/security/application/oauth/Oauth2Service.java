package com.example.promptsentinel.domain.security.application.oauth;

import com.example.promptsentinel.domain.security.dto.user.UserInfo;

public interface Oauth2Service {
    UserInfo getUserInfo(String socialAccessToken);
}
