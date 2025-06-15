package com.example.promptsentinel.domain.evalutation.dao;

import com.example.promptsentinel.domain.evalutation.entity.Evaluation;
import com.example.promptsentinel.domain.evalutation.entity.ModelEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelEvaluationRepository extends JpaRepository<ModelEvaluation, Long> {
    List<ModelEvaluation> findByEvaluation(Evaluation evaluation);
}
