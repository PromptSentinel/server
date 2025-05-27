package com.example.promptsentinel.domain.model.controller;

import com.example.promptsentinel.domain.model.dto.LLMModelRequest;
import com.example.promptsentinel.domain.model.dto.LLMResponse;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import com.example.promptsentinel.domain.model.service.LLMClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class LLMClientController {

    private final LLMClientService llmClientService;


    //추후 response 변경
    @PostMapping("/process")
    public ResponseEntity<LLMResponse> evaluatePromptAgainstLLM(@AuthenticationPrincipal Long memberId, @RequestBody LLMModelRequest request) {

        LLMResponse response  = llmClientService.evaluatePromptAgainstLLM(memberId, request);

        //System.out.println(response.getLlmResponse() + " !! ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PostMapping("/evaluate")
    public ResponseEntity<List<LLMResponse>> evaluatePromptsAgainstLLM(@AuthenticationPrincipal Long memberId, @RequestBody LLMModelRequest request) {

        List<LLMResponse> responseList  = llmClientService.evaluatePromptListAgainstLLM(memberId, request);

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


}