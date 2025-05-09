package com.example.promptsentinel.domain.model.dto;


import com.example.promptsentinel.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LLMModelRequest {
    private String modelName;
    private String modelUrl;
    private String apiKey;
    private List<String> headerList;
    private String requestFormat;
    private String responseFormat;
}
