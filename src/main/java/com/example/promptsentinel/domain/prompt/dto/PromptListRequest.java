package com.example.promptsentinel.domain.prompt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PromptListRequest {
    private List<PromptRequeat> promptRequeatList;
}
