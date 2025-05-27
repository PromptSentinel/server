package com.example.promptsentinel.domain.model.service;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.member.service.MemberService;
import com.example.promptsentinel.domain.model.dao.LLMModelRepository;
import com.example.promptsentinel.domain.model.dto.LLMModelRequest;
import com.example.promptsentinel.domain.model.dto.LLMResponse;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import com.example.promptsentinel.domain.prompt.service.PromptService;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class LLMClientService {

    private final LLMModelRepository llmModelRepository;
    private final PromptService promptService;
    private final MemberService memberService;


    //추후 프롬프트, return 값 변경
    public LLMResponse evaluatePromptAgainstLLM(Long memberId, LLMModelRequest request) {
        LLMModel llmModel = saveModel(memberId, request);
        String response = sendPrompt(llmModel, "너는 누구니?");

        log.info(response);
        return LLMResponse.builder()
                .llmResponse(response)
                .build();
    }


    //promptList들을 통해 LLMResponse List 반환
    public List<LLMResponse> evaluatePromptListAgainstLLM(Long memberId, LLMModelRequest request) {
        LLMModel llmModel = saveModel(memberId, request);
        List<Prompt> promptList = promptService.getQuestion();

        List<LLMResponse> llmResponseList = new ArrayList<>();
        for(Prompt prompt : promptList) {
            String response = sendPrompt(llmModel, prompt.getQuestion());
            log.info(response);
            LLMResponse llmResponse = LLMResponse.builder()
                    .llmRequest(prompt.getQuestion())
                    .llmResponse(response)
                    .build();

            llmResponseList.add(llmResponse);
        }

        return llmResponseList;
    }



    public String sendPrompt(LLMModel llmModel, String prompt) {
        try {
            // 요청 헤더 설정
            HttpHeaders headers = createHeaders(llmModel);

            // 요청 본문 생성
            String requestBody = createRequestBody(llmModel, prompt);

            // HTTP 요청 생성
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);


            //api 요청
            ClientResponse clientResponse = WebClient.create()
                    .method(HttpMethod.POST)
                    .uri(llmModel.getModelUrl())
                    .headers(httpHeaders -> httpHeaders.addAll(headers))  // HttpHeaders 객체 전달
                    .bodyValue(requestBody)  // body 설정
                    .exchange()
                    .block();

            ResponseEntity<String> responseEntity = ResponseEntity
                    .status(clientResponse.statusCode())
                    .headers(clientResponse.headers().asHttpHeaders())
                    .body(clientResponse.bodyToMono(String.class).block());


            log.info(responseEntity.getBody());

            return getResponse(responseEntity.getBody(), llmModel.getAttributeName());

        } catch (Exception e) {
            throw new CustomException(ErrorCode.LLM_API_FAILED);
        }

    }


    //사용자에게 입력받은 headerList로 header 생성
    private HttpHeaders createHeaders(LLMModel llmModel) {
        HttpHeaders headers = new HttpHeaders();


        List<String> headerList = llmModel.getHeaderList();
        for (String header : headerList) {
            if (header == null || !header.contains(":")) continue;


            String[] keyValue = header.split(":", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            if(key.equals("Authorization")){
                headers.setBearerAuth(llmModel.getAPIKey());
            }else {
                headers.add(key, value);
            }

        }

        log.info("header " + headers.toString());

        return headers;
    }


    //사용자에게 입력받은 request로 body 생성
    private String createRequestBody(LLMModel llmModel, String prompt) throws JsonProcessingException {
        String requestTemplate = llmModel.getRequestFormat();

        log.info("request template" + requestTemplate);
        return requestTemplate.replace("\"prompt\"", "\"" + prompt + "\"");
    }

    private String getResponse(String responseBody, String attributeName) {
        try {
            log.info("responseBody" + responseBody);
            Pattern pattern = Pattern.compile("\"" + attributeName + "\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(responseBody);
            log.info("matcher " + matcher.toString());
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.LLM_PARSE_FAIRED);
        }
        return null;
    }


    //LLMModel 객체 생성
    private LLMModel saveModel(Long memberId, LLMModelRequest request) {
        Member member = memberService.getMember(memberId);
        LLMModel model = LLMModel.builder()
                .member(member)
                .modelName(request.getModelName())
                .modelUrl(request.getModelUrl())
                .APIKey(request.getApiKey())
                .headerList(request.getHeaderList())
                .requestFormat(request.getRequestFormat())
                .responseFormat(request.getResponseFormat())
                .attributeName(getAttributeName(request.getResponseFormat()))
                .build();

        return llmModelRepository.save(model);
    }

    //response formnat에서 attributeName 추출
    public String getAttributeName(String responseFormat){
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"response_text\"");
        Matcher matcher = pattern.matcher(responseFormat);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new CustomException(ErrorCode.ATTROBUTE_NOT_FOUND);
    }


}