package com.example.promptsentinel.domain.fastAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FastApiService {

    private final RestTemplate restTemplate;

    private static final String FASTAPI_BASE_URL = "http://localhost:8000";

    /**
     * CSV 파일을 FastAPI로 전송하여 분류 결과를 받아옴
     */
    public ResponseEntity<byte[]> sendCsvToFastApi(String csvFilePath) throws IOException {
        String url = FASTAPI_BASE_URL + "/predict-csv-multi";

        // MultiValueMap을 사용하여 multipart/form-data 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 파일을 Resource로 변환
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("CSV 파일을 찾을 수 없습니다: " + csvFilePath);
        }

        FileSystemResource fileResource = new FileSystemResource(csvFile);
        body.add("file", fileResource);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        try {
            // FastAPI로 요청 전송
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            return response;
        } catch (Exception e) {
            throw new RuntimeException("FastAPI 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 단일 텍스트 분류 요청
     */
    public Map<String, Object> predictSingleText(String text) {
        String url = FASTAPI_BASE_URL + "/predict-multi";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("response", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity =
                new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("FastAPI 단일 텍스트 분류 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }
}


