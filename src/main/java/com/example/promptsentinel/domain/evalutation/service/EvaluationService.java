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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationService {
    private EvaluationRepository evaluationRepository;
    private MemberRepository memberRepository;
    public EvaluationListResponse getEvaluaionList(Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        List<Evaluation> evaluationList = evaluationRepository.findByMemberOrElseThrow(member);
        EvaluationListResponse evaluationListResponse = new EvaluationListResponse();
        evaluationListResponse.setEvaluationResponseList(
                evaluationList.stream()
                        .map(evaluation -> EvaluationResponse.builder()
                                .id(evaluation.getId())
                                .modelName(evaluation.getLlmModel().getModelName())
                                .flag(evaluation.getPercentage())
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
                .percentage(evaluation.getPercentage())
                .promptEntity(evaluation.getPromptEntity())
                .member(evaluation.getMember())
                .build();
    }

    public EvaluationDetailResponse saveEvaluation(LLMModel llmModel, List<CsvData> csvDataList){

        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .llmModel(llmModel)
                .member(llmModel.getMember())
                .percentage(0.0)
                .promptEntity(csvDataList)
                .build());

        return EvaluationDetailResponse.builder()
                .id(evaluation.getId())
                .llmModel(evaluation.getLlmModel())
                .promptEntity(evaluation.getPromptEntity())
                .member(evaluation.getMember())
                .percentage(evaluation.getPercentage())
                .build();
    }
}
