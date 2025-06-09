package com.example.promptsentinel.domain.evalutation;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PromptPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column
    private String prompt;

    @Column
    private String response;


    @Column
    private String strategy;

    @Column
    private boolean evaluationResult;

}
