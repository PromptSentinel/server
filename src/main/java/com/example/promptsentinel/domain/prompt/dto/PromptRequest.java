package com.example.promptsentinel.domain.prompt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptRequest {

    private String scenarioName;
    private String strategy;
    private String question;
    private String generatedPrompt;
}
