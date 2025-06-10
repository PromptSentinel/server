package com.example.promptsentinel.domain.prompt.dao;


import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    Optional<Prompt> findById(Long memberId);


    default Prompt findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROMPT_NOT_FOUND));
    }
}
