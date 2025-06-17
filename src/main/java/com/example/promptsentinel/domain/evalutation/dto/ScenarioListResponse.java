package com.example.promptsentinel.domain.evalutation.dto;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ScenarioListResponse {
    private String strategy;
    private String scenario;

    @Column
    private String RoBERTaLabelErrorCount;

    @Column
    private String DeBERTaLabelErrorCount;

    @Column
    private String BARTLabelErrorCount;

    @Column
    private String ELECTRALabelErrorCount;
    List<CsvData> csvDataList;
}
