package com.example.promptsentinel.domain.prompt.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptRequeat {

    private String scenarioName;
    private String strategy;
    private String question;
    private String generatedPrompt;
}
