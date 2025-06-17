package com.example.promptsentinel.domain.evalutation.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StrategyResponse {
    private String strategy;

    private String RoBERTaLabelErrorCount;

    private String DeBERTaLabelErrorCount;
    private String BARTLabelErrorCount;
    private String ELECTRALabelErrorCount;
    private List<ScenarioListResponse> scenarioListResponseList;
}
