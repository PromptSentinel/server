package com.example.promptsentinel.domain.security.api;

import com.example.promptsentinel.domain.security.application.AuthService;
import com.example.promptsentinel.domain.security.dto.request.RegisterRequest;
import com.example.promptsentinel.domain.security.dto.response.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RestTemplate restTemplate;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> loginByKakao(
            @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authService.signIn(registerRequest), HttpStatus.OK);
    }

    @GetMapping("/register")
    public ResponseEntity<?> handleOAuthRedirect(@RequestParam("code") String code) {
        String postUrl = "http://localhost:8080/api/v1/auth/register";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("providerName", "kakao");
        requestBody.put("code", code);

        // 2. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 4. POST 요청 보내기
            ResponseEntity<String> response = restTemplate.postForEntity(postUrl, requestEntity, String.class);

            // 응답 반환 or 리디렉션
            return ResponseEntity.ok(Map.of(
                    "message", "POST 요청 성공",
                    "response", response.getBody()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "POST 요청 실패",
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/withdraw")
    public void withdraw() {
        authService.withdraw();
    }



}

