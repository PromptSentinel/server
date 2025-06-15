package com.example.promptsentinel.domain.prompt.controller;

import com.example.promptsentinel.domain.prompt.dto.PromptListRequest;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import com.example.promptsentinel.domain.prompt.service.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/prompts")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    @PostMapping
    public ResponseEntity<List<Prompt>> uploadPrompt(@RequestBody PromptListRequest promptListRequest) {
        List<Prompt> promptList = promptService.uploadPrompt(promptListRequest);


        return new ResponseEntity<>(promptList, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> importPrompts(@RequestParam("file") MultipartFile file) {

        promptService.importPromptsFromMultipartFile(file);
        return ResponseEntity.ok("CSV 데이터가 성공적으로 저장되었습니다.");
    }
}
