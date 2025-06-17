package com.example.promptsentinel.domain.evalutation.dao;

import com.example.promptsentinel.domain.evalutation.entity.Evaluation;
import com.example.promptsentinel.domain.evalutation.entity.StrategyEvaluation;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StrategyEvaluationRepository extends JpaRepository<StrategyEvaluation, Long> {

    Optional<List<StrategyEvaluation>> findByEvaluation(Evaluation evaluation);

    Optional<List<StrategyEvaluation>> findByEvaluationAndStrategy(Evaluation evaluation, String strategy);

    default List<StrategyEvaluation> findByEvaluationOrElseThrow(Evaluation evaluation) {
        return findByEvaluation(evaluation)
                .orElseThrow(() -> new CustomException(ErrorCode.STRATEGY_NOT_FOUND));
    }

    default List<StrategyEvaluation> findByEvaluationAndStrategyOrElseThrow(Evaluation evaluation, String strategy) {
        return findByEvaluationAndStrategy(evaluation, strategy)
                .orElseThrow(() -> new CustomException(ErrorCode.STRATEGY_NOT_FOUND));
    }
}
