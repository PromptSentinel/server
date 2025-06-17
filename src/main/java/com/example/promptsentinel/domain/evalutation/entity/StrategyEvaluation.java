package com.example.promptsentinel.domain.evalutation.entity;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "strategy_evaluation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    @Column
    private String strategy;

    @Column
    private String RoBERTaLabelErrorCount;

    @Column
    private String DeBERTaLabelErrorCount;

    @Column
    private String BARTLabelErrorCount;

    @Column
    private String ELECTRALabelErrorCount;

    @OneToMany
    @JoinTable(
            name = "evaluation_prompt_entity",
            joinColumns = @JoinColumn(name = "evaluation_id"),
            inverseJoinColumns = @JoinColumn(name = "prompt_entity_id")
    )
    private List<CsvData> promptEntity;

}
