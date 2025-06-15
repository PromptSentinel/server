package com.example.promptsentinel.domain.evalutation.dto;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EvaluationDetailResponse {
    Long id;

    private Member member;

    private LLMModel llmModel;

    private String RoBERTaLabelErrorCount;

    private String DeBERTaLabelErrorCount;

    private String BARTLabelErrorCount;

    private String ELECTRALabelErrorCount;

    private List<CsvData> promptEntity;

    private List<ModelEvaluationResponse> modelEvaluationResponses;
}
