package com.example.promptsentinel.domain.prompt.service;

import com.example.promptsentinel.domain.evalutation.dto.EvaluationDetailResponse;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import com.example.promptsentinel.domain.model.service.LLMClientService;
import com.example.promptsentinel.domain.prompt.dao.PromptRepository;
import com.example.promptsentinel.domain.prompt.dto.PromptListRequest;
import com.example.promptsentinel.domain.prompt.dto.PromptRequeat;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptRepository promptRepository;
    private final LLMClientService llmClientService;

    public List<Prompt> getQuestion(){
        return promptRepository.findAll();
    }

    public List<Prompt> uploadPrompt(PromptListRequest promptListRequest) {
        return promptListRequest.getPromptRequeatList().stream()
                .map(promptRequeat -> promptRepository.save(
                        Prompt.builder()
                                .strategy(promptRequeat.getStaregy())
                                .question(promptRequeat.getPrompt())
                                .build()))
                .collect(Collectors.toList());
    }

//    public EvaluationDetailResponse getResult(LLMModel llmModel){
//        List<Prompt> promptList = getQuestion();
//
//    }
}
