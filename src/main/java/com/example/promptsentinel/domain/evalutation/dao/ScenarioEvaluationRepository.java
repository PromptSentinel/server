package com.example.promptsentinel.domain.evalutation.dao;

import com.example.promptsentinel.domain.evalutation.entity.Evaluation;
import com.example.promptsentinel.domain.evalutation.entity.ScenarioEvaluation;
import com.example.promptsentinel.domain.evalutation.entity.StrategyEvaluation;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScenarioEvaluationRepository extends JpaRepository<ScenarioEvaluation, Long> {
    Optional<List<ScenarioEvaluation>> findByEvaluation(Evaluation evaluation);


    Optional<List<ScenarioEvaluation>> findByEvaluationAndStrategy(Evaluation evaluation, String strategy);
    default List<ScenarioEvaluation> findByEvaluationAndStrategyOrElseThrow(Evaluation evaluation, String strategy) {
        return findByEvaluationAndStrategy(evaluation, strategy)
                .orElseThrow(() -> new CustomException(ErrorCode.SCENARIO_NOT_FOUND));
    }
}
