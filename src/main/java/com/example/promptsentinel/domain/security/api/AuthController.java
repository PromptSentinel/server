package com.example.promptsentinel.domain.security.api;

import com.example.promptsentinel.domain.security.application.AuthService;
import com.example.promptsentinel.domain.security.dto.request.RegisterRequest;
import com.example.promptsentinel.domain.security.dto.response.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> loginByKakao(
            @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authService.signIn(registerRequest), HttpStatus.OK);
    }


}

