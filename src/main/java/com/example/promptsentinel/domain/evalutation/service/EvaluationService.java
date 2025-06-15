package com.example.promptsentinel.domain.evalutation.service;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.evalutation.Evaluation;
import com.example.promptsentinel.domain.evalutation.dao.EvaluationRepository;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationDetailResponse;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationListResponse;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationResponse;
import com.example.promptsentinel.domain.member.dao.MemberRepository;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;
    public EvaluationListResponse getEvaluaionList(Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        List<Evaluation> evaluationList = evaluationRepository.findByMemberOrElseThrow(member);
        EvaluationListResponse evaluationListResponse = new EvaluationListResponse();
        evaluationListResponse.setEvaluationResponseList(
                evaluationList.stream()
                        .map(evaluation -> EvaluationResponse.builder()
                                .id(evaluation.getId())
                                .modelName(evaluation.getLlmModel().getModelName())
                                .RoBERTaLabelErrorCount(evaluation.getRoBERTaLabelErrorCount())
                                .DeBERTaLabelErrorCount(evaluation.getDeBERTaLabelErrorCount())
                                .BARTLabelErrorCount(evaluation.getBARTLabelErrorCount())
                                .ELECTRALabelErrorCount(evaluation.getELECTRALabelErrorCount())
                                .member(evaluation.getMember())
                                .build())
                        .collect(Collectors.toList())
        );
        return evaluationListResponse;
    }

    public EvaluationDetailResponse getEvaluationDetail(Long memberId, Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findByIdOrElseThrow(evaluationId);


        return EvaluationDetailResponse.builder()
                .id(evaluationId)
                .llmModel(evaluation.getLlmModel())
                .RoBERTaLabelErrorCount(evaluation.getRoBERTaLabelErrorCount())
                .DeBERTaLabelErrorCount(evaluation.getDeBERTaLabelErrorCount())
                .BARTLabelErrorCount(evaluation.getBARTLabelErrorCount())
                .ELECTRALabelErrorCount(evaluation.getELECTRALabelErrorCount())
                .promptEntity(evaluation.getPromptEntity())
                .member(evaluation.getMember())
                .build();
    }

    public EvaluationDetailResponse saveEvaluation(LLMModel llmModel, List<CsvData> csvDataList){

        long zeroCountRoBERTaLabel = csvDataList.stream()
                .filter(data -> data.getRoBERTaLabel() == 1)
                .count();

        long zeroCountDeBERTaLabel = csvDataList.stream()
                .filter(data -> data.getDeBERTaLabel() == 1)
                .count();


        long zeroCountBARTLabel = csvDataList.stream()
                .filter(data -> data.getBARTLabel() == 1)
                .count();

        long zeroCountELECTRALabel = csvDataList.stream()
                .filter(data -> data.getELECTRALabel() == 1)
                .count();


        int size = csvDataList.size();

        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .llmModel(llmModel)
                .member(llmModel.getMember())
                .RoBERTaLabelErrorCount(zeroCountRoBERTaLabel+"/"+size)
                .DeBERTaLabelErrorCount(zeroCountDeBERTaLabel+"/"+size)
                .BARTLabelErrorCount(zeroCountBARTLabel+"/"+size)
                .ELECTRALabelErrorCount(zeroCountELECTRALabel+"/"+size)
                .promptEntity(csvDataList)
                .build());

        return EvaluationDetailResponse.builder()
                .id(evaluation.getId())
                .llmModel(evaluation.getLlmModel())
                .promptEntity(evaluation.getPromptEntity())
                .member(evaluation.getMember())
                .RoBERTaLabelErrorCount(evaluation.getRoBERTaLabelErrorCount())
                .DeBERTaLabelErrorCount(evaluation.getDeBERTaLabelErrorCount())
                .BARTLabelErrorCount(evaluation.getBARTLabelErrorCount())
                .ELECTRALabelErrorCount(evaluation.getELECTRALabelErrorCount())
                .build();
    }
}
