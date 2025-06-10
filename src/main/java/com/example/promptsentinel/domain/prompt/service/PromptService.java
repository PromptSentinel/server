package com.example.promptsentinel.domain.prompt.service;

import com.example.promptsentinel.domain.prompt.dao.PromptRepository;
import com.example.promptsentinel.domain.prompt.dto.PromptListRequest;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptRepository promptRepository;

    public List<Prompt> getQuestion(){
        return promptRepository.findAll();
    }

    public List<Prompt> uploadPrompt(PromptListRequest promptListRequest) {
        return promptListRequest.getPromptRequeatList().stream()
                .map(promptRequeat -> promptRepository.save(
                        Prompt.builder()
                                .scenarioName(promptRequeat.getScenarioName())
                                .strategy(promptRequeat.getStrategy())
                                .question(promptRequeat.getQuestion())
                                .generatedPrompt(promptRequeat.getGeneratedPrompt())
                                .build()))
                .collect(Collectors.toList());
    }

//    public EvaluationDetailResponse getResult(LLMModel llmModel){
//        List<Prompt> promptList = getQuestion();
//
//    }
}
