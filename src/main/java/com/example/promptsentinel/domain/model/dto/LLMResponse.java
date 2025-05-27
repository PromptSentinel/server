package com.example.promptsentinel.domain.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LLMResponse {
    private String llmRequest;
    private String llmResponse;
}
