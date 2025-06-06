package com.example.promptsentinel.domain.evalutation.dto;

import com.example.promptsentinel.domain.evalutation.PromptPair;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EvaluationDetailResponse {
    Long id;

    private Member member;

    private LLMModel llmModel;

    private Double percentage;

    private List<PromptPair> promptPair;
}
