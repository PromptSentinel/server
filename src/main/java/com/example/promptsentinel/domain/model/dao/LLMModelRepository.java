package com.example.promptsentinel.domain.model.dao;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LLMModelRepository extends JpaRepository<LLMModel, Long> {

}
