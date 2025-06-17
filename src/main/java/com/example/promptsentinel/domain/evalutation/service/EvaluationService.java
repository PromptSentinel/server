package com.example.promptsentinel.domain.evalutation.service;

import com.example.promptsentinel.domain.csv.dao.CsvDataRepository;
import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.evalutation.dao.EvaluationRepository;
import com.example.promptsentinel.domain.evalutation.dao.ScenarioEvaluationRepository;
import com.example.promptsentinel.domain.evalutation.dao.StrategyEvaluationRepository;
import com.example.promptsentinel.domain.evalutation.dto.*;
import com.example.promptsentinel.domain.evalutation.entity.Evaluation;
import com.example.promptsentinel.domain.evalutation.entity.ScenarioEvaluation;
import com.example.promptsentinel.domain.evalutation.entity.StrategyEvaluation;
import com.example.promptsentinel.domain.member.dao.MemberRepository;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import jakarta.persistence.Column;
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
    private final ScenarioEvaluationRepository scenarioEvaluationRepository;
    private final StrategyEvaluationRepository strategyEvaluationRepository;
    private final CsvDataRepository csvDataRepository;
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
                .strategyResponseList(getStrategyResponse(evaluation))
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
                //.promptEntity(csvDataList)
                .build());

        saveStrategyAndScenarioEvaluation(csvDataList, evaluation);



        return EvaluationDetailResponse.builder()
                .id(evaluation.getId())
                .llmModel(evaluation.getLlmModel())
                .strategyResponseList(getStrategyResponse(evaluation))
                .member(evaluation.getMember())
                .RoBERTaLabelErrorCount(evaluation.getRoBERTaLabelErrorCount())
                .DeBERTaLabelErrorCount(evaluation.getDeBERTaLabelErrorCount())
                .BARTLabelErrorCount(evaluation.getBARTLabelErrorCount())
                .ELECTRALabelErrorCount(evaluation.getELECTRALabelErrorCount())
                .build();
    }
    private List<StrategyResponse> getStrategyResponse(Evaluation evaluation){
        List<StrategyEvaluation> strategyEvaluationList = strategyEvaluationRepository.findByEvaluationOrElseThrow(evaluation);

        List<StrategyResponse> strategyResponseList = new ArrayList<>();

        for(StrategyEvaluation strategyEvaluation : strategyEvaluationList) {
            StrategyResponse strategyResponse = StrategyResponse.builder()
                    .strategy(strategyEvaluation.getStrategy())
                    .BARTLabelErrorCount(strategyEvaluation.getBARTLabelErrorCount())
                    .DeBERTaLabelErrorCount(strategyEvaluation.getDeBERTaLabelErrorCount())
                    .RoBERTaLabelErrorCount(strategyEvaluation.getRoBERTaLabelErrorCount())
                    .ELECTRALabelErrorCount(strategyEvaluation.getELECTRALabelErrorCount())
                    .scenarioListResponseList(getScenarioListResponse(evaluation, strategyEvaluation.getStrategy()))
                    .build();

            strategyResponseList.add(strategyResponse);

        }
        return strategyResponseList;


    }

    private List<ScenarioListResponse> getScenarioListResponse(Evaluation evaluation, String strategy){
        List<ScenarioEvaluation> scenarioEvaluationList = scenarioEvaluationRepository.findByEvaluationAndStrategyOrElseThrow(evaluation, strategy);

        List<ScenarioListResponse> scenarioListResponseList = new ArrayList<>();

        for(ScenarioEvaluation scenarioEvaluation : scenarioEvaluationList){
            ScenarioListResponse scenarioListResponse = ScenarioListResponse.builder()
                    .scenario(scenarioEvaluation.getScenarioName())
                    .strategy(scenarioEvaluation.getStrategy())
                    .BARTLabelErrorCount(scenarioEvaluation.getBARTLabelErrorCount())
                    .DeBERTaLabelErrorCount(scenarioEvaluation.getDeBERTaLabelErrorCount())
                    .RoBERTaLabelErrorCount(scenarioEvaluation.getRoBERTaLabelErrorCount())
                    .ELECTRALabelErrorCount(scenarioEvaluation.getELECTRALabelErrorCount())
                    .csvDataList(scenarioEvaluation.getPromptEntity())
                    .build();

            scenarioListResponseList.add(scenarioListResponse);
        }
        return scenarioListResponseList;
    }


    private void saveStrategyAndScenarioEvaluation(List<CsvData> csvDataList, Evaluation evaluation) {
        int strategySize = 0;
        int RoBERTaLabelStrategyCount = 0;
        int DeBERTaLabelStrategyCount = 0;
        int BARTLabelStrategyCount = 0;
        int ELECTRALabelStrategyCount = 0;


        int scenarioSize = 0;
        int RoBERTaLabelScenarioCount = 0;
        int DeBERTaLabelScenarioCount = 0;
        int BARTLabelScenarioCount = 0;
        int ELECTRALabelScenarioCount = 0;


        String strategy = csvDataList.get(0).getStrategy();
        String scenario = csvDataList.get(0).getScenarioName();

        List<CsvData> csvDatas = new ArrayList<>();

        for(CsvData csvData : csvDataList){
            String currentStrategy = csvData.getStrategy();
            String currentScenario = csvData.getScenarioName();

            if(!strategy.equals(currentStrategy)){
                log.info("strategy : 1111");
                strategy = currentStrategy;

                strategyEvaluationRepository.save(StrategyEvaluation.builder()
                        .evaluation(evaluation)
                        .strategy(strategy)
                        .RoBERTaLabelErrorCount(RoBERTaLabelStrategyCount+"/"+strategySize)
                        .DeBERTaLabelErrorCount(DeBERTaLabelStrategyCount+"/"+strategySize)
                        .BARTLabelErrorCount(BARTLabelStrategyCount+"/"+strategySize)
                        .ELECTRALabelErrorCount(ELECTRALabelStrategyCount+"/"+strategySize)
                        .build()
                );


                RoBERTaLabelStrategyCount = 0;
                DeBERTaLabelStrategyCount = 0;
                BARTLabelStrategyCount = 0;
                ELECTRALabelStrategyCount = 0;
                strategySize = 0;
            }



            if(!scenario.equals(currentScenario)){
                scenario = currentScenario;
                log.info("scenario : 1111");

                log.info("csvData : "+csvDataList.size());
                scenarioEvaluationRepository.save(ScenarioEvaluation.builder()
                        .evaluation(evaluation)
                        .strategy(strategy)
                        .scenarioName(scenario)
                        .RoBERTaLabelErrorCount(RoBERTaLabelScenarioCount+"/"+scenarioSize)
                        .DeBERTaLabelErrorCount(DeBERTaLabelScenarioCount+"/"+scenarioSize)
                        .BARTLabelErrorCount(BARTLabelScenarioCount+"/"+scenarioSize)
                        .ELECTRALabelErrorCount(ELECTRALabelScenarioCount+"/"+scenarioSize)
                        .promptEntity(csvDatas)
                        .build()
                );

                csvDatas = new ArrayList<>();
                RoBERTaLabelScenarioCount = 0;
                DeBERTaLabelScenarioCount = 0;
                BARTLabelScenarioCount = 0;
                ELECTRALabelScenarioCount = 0;
                scenarioSize = 0;
            }


            csvDatas.add(csvData);
            strategySize++;
            scenarioSize++;
            if(csvData.getRoBERTaLabel()==1){
                RoBERTaLabelStrategyCount++;
                RoBERTaLabelScenarioCount++;
            }
            if(csvData.getDeBERTaLabel()==1){
                DeBERTaLabelStrategyCount++;
                DeBERTaLabelScenarioCount++;
            }
            if(csvData.getBARTLabel()==1){
                BARTLabelStrategyCount++;
                BARTLabelScenarioCount++;
            }
            if(csvData.getELECTRALabel()==1){
                ELECTRALabelStrategyCount++;
                ELECTRALabelScenarioCount++;
            }
        }
        strategyEvaluationRepository.save(StrategyEvaluation.builder()
                .evaluation(evaluation)
                .strategy(strategy)
                .RoBERTaLabelErrorCount(RoBERTaLabelStrategyCount+"/"+strategySize)
                .DeBERTaLabelErrorCount(DeBERTaLabelStrategyCount+"/"+strategySize)
                .BARTLabelErrorCount(BARTLabelStrategyCount+"/"+strategySize)
                .ELECTRALabelErrorCount(ELECTRALabelStrategyCount+"/"+strategySize)
                .build()
        );

        scenarioEvaluationRepository.save(ScenarioEvaluation.builder()
                .evaluation(evaluation)
                .strategy(strategy)
                .scenarioName(scenario)
                .RoBERTaLabelErrorCount(RoBERTaLabelScenarioCount+"/"+scenarioSize)
                .DeBERTaLabelErrorCount(DeBERTaLabelScenarioCount+"/"+scenarioSize)
                .BARTLabelErrorCount(BARTLabelScenarioCount+"/"+scenarioSize)
                .ELECTRALabelErrorCount(ELECTRALabelScenarioCount+"/"+scenarioSize)
                .promptEntity(csvDatas)
                .build()
        );
    }

}
