package com.example.promptsentinel.domain.evalutation.dto;

import com.example.promptsentinel.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EvaluationResponse {
    Long id;

    private Member member;

    private String modelName;

    private Double flag;

}
