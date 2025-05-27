package com.example.promptsentinel.domain.prompt.dao;


import com.example.promptsentinel.domain.prompt.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
}
