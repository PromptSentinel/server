package com.example.promptsentinel.domain.evalutation.dto;

import com.example.promptsentinel.domain.member.entity.Member;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EvaluationResponse {
    Long id;

    private Member member;

    private String modelName;

    private String RoBERTaLabelErrorCount;

    private String DeBERTaLabelErrorCount;

    private String BARTLabelErrorCount;

    private String ELECTRALabelErrorCount;

}
