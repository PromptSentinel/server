package com.example.promptsentinel.global.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "Sample Error Message"),
    AUTH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보를 찾을 수 없습니다."),
    KAKAO_USER_INFO_FAILED(HttpStatus.BAD_REQUEST, "카카오 유저 정보 조회를 실패하였습니다."),

    MEMBER_NOT_FOUND( HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
