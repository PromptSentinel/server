package com.example.promptsentinel.domain.prompt.service;

import com.example.promptsentinel.domain.model.dto.LLMResponse;
import com.example.promptsentinel.domain.model.service.LLMClientService;
import com.example.promptsentinel.domain.prompt.dao.PromptRepository;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptRepository promptRepository;

    public List<Prompt> getQuestion(){
        return promptRepository.findAll();
    }
}
