package com.example.promptsentinel.domain.model.dao;

import com.example.promptsentinel.domain.model.entity.LLMModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LLMModelRepository extends JpaRepository<LLMModel, Long> {
}
