package com.example.promptsentinel.domain.evalutation.service;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.evalutation.dao.ModelEvaluationRepository;
import com.example.promptsentinel.domain.evalutation.dto.ModelEvaluationResponse;
import com.example.promptsentinel.domain.evalutation.entity.Evaluation;
import com.example.promptsentinel.domain.evalutation.dao.EvaluationRepository;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationDetailResponse;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationListResponse;
import com.example.promptsentinel.domain.evalutation.dto.EvaluationResponse;
import com.example.promptsentinel.domain.evalutation.entity.ModelEvaluation;
import com.example.promptsentinel.domain.member.dao.MemberRepository;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final ModelEvaluationRepository modelEvaluationRepository;
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
        List<ModelEvaluation> modelEvaluationList = modelEvaluationRepository.findByEvaluation(evaluation);

        List<ModelEvaluationResponse> modelEvaluationResponseList = modelEvaluationList.stream()
                .map(me -> ModelEvaluationResponse.builder()
                        .strategy(me.getStrategy())
                        .RoBERTaLabelErrorCount(me.getRoBERTaLabelErrorCount())
                        .DeBERTaLabelErrorCount(me.getDeBERTaLabelErrorCount())
                        .BARTLabelErrorCount(me.getBARTLabelErrorCount())
                        .ELECTRALabelErrorCount(me.getELECTRALabelErrorCount())
                        .build())
                .toList();

        return EvaluationDetailResponse.builder()
                .id(evaluationId)
                .llmModel(evaluation.getLlmModel())
                .RoBERTaLabelErrorCount(evaluation.getRoBERTaLabelErrorCount())
                .DeBERTaLabelErrorCount(evaluation.getDeBERTaLabelErrorCount())
                .BARTLabelErrorCount(evaluation.getBARTLabelErrorCount())
                .ELECTRALabelErrorCount(evaluation.getELECTRALabelErrorCount())
                .promptEntity(evaluation.getPromptEntity())
                .modelEvaluationResponses(modelEvaluationResponseList)
                .member(evaluation.getMember())
                .build();
    }

    public EvaluationDetailResponse saveEvaluation(LLMModel llmModel, List<CsvData> csvDataList){

        long oneCountRoBERTaLabel = csvDataList.stream()
                .filter(data -> data.getRoBERTaLabel() == 1)
                .count();

        long oneCountDeBERTaLabel = csvDataList.stream()
                .filter(data -> data.getDeBERTaLabel() == 1)
                .count();


        long oneCountBARTLabel = csvDataList.stream()
                .filter(data -> data.getBARTLabel() == 1)
                .count();

        long oneCountELECTRALabel = csvDataList.stream()
                .filter(data -> data.getELECTRALabel() == 1)
                .count();


        int size = csvDataList.size();

        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .llmModel(llmModel)
                .member(llmModel.getMember())
                .RoBERTaLabelErrorCount(oneCountRoBERTaLabel+"/"+size)
                .DeBERTaLabelErrorCount(oneCountDeBERTaLabel+"/"+size)
                .BARTLabelErrorCount(oneCountBARTLabel+"/"+size)
                .ELECTRALabelErrorCount(oneCountELECTRALabel+"/"+size)
                .promptEntity(csvDataList)
                .build());



        List<ModelEvaluation> modelEvaluationList = getModelEvaluationList(evaluation, csvDataList);

        evaluation.setModelEvaluations(modelEvaluationList);

        List<ModelEvaluationResponse> modelEvaluationResponseList = modelEvaluationList.stream()
                .map(me -> ModelEvaluationResponse.builder()
                        .strategy(me.getStrategy())
                        .RoBERTaLabelErrorCount(me.getRoBERTaLabelErrorCount())
                        .DeBERTaLabelErrorCount(me.getDeBERTaLabelErrorCount())
                        .BARTLabelErrorCount(me.getBARTLabelErrorCount())
                        .ELECTRALabelErrorCount(me.getELECTRALabelErrorCount())
                        .build())
                .toList();


        return EvaluationDetailResponse.builder()
                .id(evaluation.getId())
                .llmModel(evaluation.getLlmModel())
                .promptEntity(evaluation.getPromptEntity())
                .member(evaluation.getMember())
                .RoBERTaLabelErrorCount(evaluation.getRoBERTaLabelErrorCount())
                .DeBERTaLabelErrorCount(evaluation.getDeBERTaLabelErrorCount())
                .BARTLabelErrorCount(evaluation.getBARTLabelErrorCount())
                .ELECTRALabelErrorCount(evaluation.getELECTRALabelErrorCount())
                .modelEvaluationResponses(modelEvaluationResponseList)
                .build();
    }



    private List<ModelEvaluation> getModelEvaluationList(Evaluation evaluation, List<CsvData> csvDataList) {
        List<ModelEvaluation> modelEvaluationList = new ArrayList<>();

        if (csvDataList == null || csvDataList.isEmpty()) {
            return modelEvaluationList;
        }

        String currentStrategy = csvDataList.get(0).getStrategy();
        int oneCountRoBERTaLabel = 0;
        int oneCountDeBERTaLabel = 0;
        int oneCountBARTLabel = 0;
        int oneCountELECTRALabel = 0;

        int size = 0;

        for (int i = 0; i < csvDataList.size(); i++) {
            CsvData csvData = csvDataList.get(i);
            String strategy = csvData.getStrategy();

            if (!strategy.equals(currentStrategy)) {
                // 이전 strategy 그룹 저장
                ModelEvaluation modelEvaluation = ModelEvaluation.builder()
                        .evaluation(evaluation)
                        .strategy(currentStrategy)
                        .RoBERTaLabelErrorCount(oneCountRoBERTaLabel + "/" + size)
                        .DeBERTaLabelErrorCount(oneCountDeBERTaLabel + "/" + size)
                        .BARTLabelErrorCount(oneCountBARTLabel + "/" + size)
                        .ELECTRALabelErrorCount(oneCountELECTRALabel + "/" + size)
                        .build();

                modelEvaluationRepository.save(modelEvaluation);
                modelEvaluationList.add(modelEvaluation);

                // 초기화
                currentStrategy = strategy;
                oneCountRoBERTaLabel = 0;
                oneCountDeBERTaLabel = 0;
                oneCountBARTLabel = 0;
                oneCountELECTRALabel = 0;
                size = 0;
            }

            // 카운트 갱신
            size++;
            if (csvData.getRoBERTaLabel() == 1) oneCountRoBERTaLabel++;
            if (csvData.getDeBERTaLabel() == 1) oneCountDeBERTaLabel++;
            if (csvData.getBARTLabel() == 1) oneCountBARTLabel++;
            if (csvData.getELECTRALabel() == 1) oneCountELECTRALabel++;

            // 마지막 항목일 경우 처리
            if (i == csvDataList.size() - 1) {
                ModelEvaluation modelEvaluation = ModelEvaluation.builder()
                        .evaluation(evaluation)
                        .strategy(currentStrategy)
                        .RoBERTaLabelErrorCount(oneCountRoBERTaLabel + "/" + size)
                        .DeBERTaLabelErrorCount(oneCountDeBERTaLabel + "/" + size)
                        .BARTLabelErrorCount(oneCountBARTLabel + "/" + size)
                        .ELECTRALabelErrorCount(oneCountELECTRALabel + "/" + size)
                        .build();

                modelEvaluationRepository.save(modelEvaluation);
                modelEvaluationList.add(modelEvaluation);
            }
        }

        return modelEvaluationList;
    }

}
