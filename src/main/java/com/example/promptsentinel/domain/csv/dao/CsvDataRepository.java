package com.example.promptsentinel.domain.csv.dao;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.evalutation.entity.Evaluation;
import com.example.promptsentinel.domain.evalutation.entity.StrategyEvaluation;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CsvDataRepository extends JpaRepository<CsvData, Long> {

}
