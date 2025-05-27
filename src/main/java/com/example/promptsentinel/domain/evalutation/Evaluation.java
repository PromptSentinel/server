package com.example.promptsentinel.domain.evalutation;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "evaluation")
@Getter
@Setter
@NoArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "llm_model_id")
    private LLMModel llmModel;

    @Column
    private Double percentage;


    @OneToMany
    private List<PromptPair> promptPair;
}
