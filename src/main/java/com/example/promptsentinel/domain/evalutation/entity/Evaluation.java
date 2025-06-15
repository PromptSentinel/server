package com.example.promptsentinel.domain.evalutation.entity;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import com.example.promptsentinel.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation extends BaseTimeEntity {
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
    private String RoBERTaLabelErrorCount;

    @Column
    private String DeBERTaLabelErrorCount;

    @Column
    private String BARTLabelErrorCount;

    @Column
    private String ELECTRALabelErrorCount;

    @OneToMany
    @JoinColumn(name = "evaluation_id")
    private List<ModelEvaluation> modelEvaluations;


    @OneToMany
    @JoinTable(
            name = "evaluation_prompt_entity",
            joinColumns = @JoinColumn(name = "evaluation_id"),
            inverseJoinColumns = @JoinColumn(name = "prompt_entity_id")
    )
    private List<CsvData> promptEntity;
}
