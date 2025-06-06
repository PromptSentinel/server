package com.example.promptsentinel.domain.evalutation.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationListResponse {
    List<EvaluationResponse> evaluationResponseList;
}
