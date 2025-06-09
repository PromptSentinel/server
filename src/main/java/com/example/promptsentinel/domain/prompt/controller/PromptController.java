package com.example.promptsentinel.domain.prompt.controller;

import com.example.promptsentinel.domain.model.dto.LLMModelRequest;
import com.example.promptsentinel.domain.model.dto.LLMResponse;
import com.example.promptsentinel.domain.prompt.dto.PromptListRequest;
import com.example.promptsentinel.domain.prompt.dto.PromptRequeat;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import com.example.promptsentinel.domain.prompt.service.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prompt")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    @PostMapping
    public ResponseEntity<List<Prompt>> uploadPrompt(@RequestBody PromptListRequest promptListRequest) {
        List<Prompt> promptList = promptService.uploadPrompt(promptListRequest);


        return new ResponseEntity<>(promptList, HttpStatus.OK);
    }
}
