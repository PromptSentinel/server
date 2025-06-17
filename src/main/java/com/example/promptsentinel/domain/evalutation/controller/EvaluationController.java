package com.example.promptsentinel.domain.evalutation.controller;


import com.example.promptsentinel.domain.evalutation.dto.EvaluationDetailResponse;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationListResponse;
import com.example.promptsentinel.domain.evalutation.service.EvaluationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;
    @GetMapping
    public ResponseEntity<EvaluationListResponse> getEvaluationList(@AuthenticationPrincipal Long memberId){
        EvaluationListResponse evaluationListResponse = evaluationService.getEvaluaionList(memberId);
        return new ResponseEntity<>(evaluationListResponse, HttpStatus.OK);
    }

}
